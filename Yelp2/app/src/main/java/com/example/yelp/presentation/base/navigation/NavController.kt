package com.example.yelp.presentation.base.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.yelp.domain.navModels.BaseArg
import org.json.JSONArray
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

class NavController(
    private val fragmentManger: FragmentManager,
    @IdRes private val containerId: Int,
    val rootFragmentListener: RootFragmentListener
) {

    private val fragmentStacksTags: MutableList<Stack<String>> = ArrayList()
    private var mCurrentFrag: Fragment? = null

    var currentStackIndex: Int = TAB1
        private set
    private var tagCount: Int = 0
    private val fragmentCache = mutableMapOf<String, WeakReference<Fragment>>()

    val currentFrag: Fragment?
        get() {
            // Attempt to used stored current fragment
            if (mCurrentFrag?.isAdded == true && mCurrentFrag?.isDetached?.not() == true) {
                return mCurrentFrag
            } else if (currentStackIndex == NO_TAB) {
                return null
            } else if (fragmentStacksTags.isEmpty()) {
                return null
            }
            // if not, try to pull it from the stack
            val fragmentStack = fragmentStacksTags[currentStackIndex]
            if (!fragmentStack.isEmpty()) {
                val fragmentByTag = getFragment(fragmentStack.peek())
                if (fragmentByTag != null) {
                    mCurrentFrag = fragmentByTag
                }
            }
            return mCurrentFrag
        }

    val currentStack: Stack<Fragment>?
        get() = getStack(currentStackIndex)

    private fun getStack(index: Int): Stack<Fragment>? {
        if (index == NO_TAB) {
            return null
        }
        return fragmentStacksTags[index].mapNotNullTo(Stack()) { s -> getFragment(s) }
    }

    val isLastInStack: Boolean
        get() = currentStack!!.size <= 1

    fun initialize(index: Int = TAB1, savedInstanceState: Bundle? = null) {

        val numberOfTabs: Int = rootFragmentListener.numberOfRootFragments

        if (!restoreFromBundle(savedInstanceState)) {
            fragmentStacksTags.clear()
            for (i in 0 until numberOfTabs) {
                fragmentStacksTags.add(Stack())
            }
            currentStackIndex = index
            if (currentStackIndex > fragmentStacksTags.size) {
                throw IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks")
            }
            clearFragmentManager()
            if (index == NO_TAB) {
                return
            }

            val ft = createTransaction(options = null, isPopping = false, animated = false)

            val lowerBound = index
            val upperBound = index + 1
            for (i in lowerBound until upperBound) {
                currentStackIndex = i
                val fragment = getRootFragment(i)
                val fragmentTag = generateTag(fragment)
                fragmentStacksTags[currentStackIndex].push(fragmentTag)
                ft.addSafe(containerId, fragment, fragmentTag)
                if (i != index) {
                    ft.detach(fragment)
                } else {
                    mCurrentFrag = fragment
                }
            }
            currentStackIndex = index

            ft.commit()
        }
    }

    fun switchTab(
        index: Int,
        options: NavTransactionOptions? = null,
        clearStack: Boolean = false
    ) {
        // Check to make sure the tab is within range
        if (index >= fragmentStacksTags.size) {
            throw IndexOutOfBoundsException(
                "Can't switch to a tab that hasn't been initialized, " +
                        "Index : " + index + ", current stack size : " + fragmentStacksTags.size +
                        ". Make sure to create all of the tabs you need in the Constructor or provide a way for them to be created via RootFragmentListener."
            )
        }
        if (currentStackIndex != index) {
            val ft = createTransaction(options = options, isPopping = index < currentStackIndex)

            ft.detachCurrentFragment()

            currentStackIndex = index

            var fragment: Fragment? = null
            if (index == NO_TAB) {
                ft.commit()
            } else {
                // Attempt to reattach previous fragment
                fragment = addPreviousFragment(ft)
                ft.commit()
            }
            mCurrentFrag = fragment
        } else {
            if (clearStack) {
                clearStack(index)
            }
        }
    }

    private fun addPreviousFragment(ft: FragmentTransaction): Fragment {
        val fragmentStack = fragmentStacksTags[currentStackIndex]
        var currentFragment: Fragment? = null
        var currentTag: String? = null
        var index = 0
        val initialSize = fragmentStack.size
        while (currentFragment == null && fragmentStack.isNotEmpty()) {
            index++
            currentTag = fragmentStack.pop()
            currentFragment = getFragment(currentTag)
        }
        return if (currentFragment != null) {
            if (index > 1) {
                Timber.d("Could not restore top fragment on current stack")
            }
            fragmentStack.push(currentTag)
            ft.attach(currentFragment)

            currentFragment
        } else {
            if (initialSize > 0) {
                Timber.d("Could not restore any fragment on current stack, adding new root fragment")
            }
            val rootFragment = getRootFragment(currentStackIndex)
            val rootTag = generateTag(rootFragment)
            fragmentStack.push(rootTag)
            ft.addSafe(containerId, rootFragment, rootTag)
            rootFragment
        }
    }

    fun clearStack(tabIndex: Int, options: NavTransactionOptions? = null) {
        if (tabIndex == NO_TAB) {
            return
        }

        // Grab Current stack
        val fragmentStack = fragmentStacksTags[tabIndex]

        // Only need to start popping and reattach if the stack is greater than 1
        if (fragmentStack.size > 1) {
            val shouldAnimate = tabIndex == currentStackIndex
            val ft =
                createTransaction(options = options, isPopping = true, animated = shouldAnimate)

            // Pop all of the fragments on the stack and remove them from the FragmentManager
            while (fragmentStack.size > 1) {
                val fragment = getFragment(fragmentStack.pop())
                if (fragment != null) {
                    ft.removeSafe(fragment)
                }
            }

            // Attempt to reattach previous fragment
            val fragment = addPreviousFragment(ft)
            ft.commit()

            mCurrentFrag = fragment
        }
    }

    fun pushFragment(fragment: Fragment?, options: NavTransactionOptions? = null) {
        if (fragment != null && currentStackIndex != NO_TAB) {

            if (fragment.isTheSameWith(currentFrag)) {
                return
            }

            val fragmentTag = generateTag(fragment)
            createTransaction(options = options, isPopping = false)
                .detachCurrentFragment()
                .removeDuplicate(fragment)
                .addSafe(containerId, fragment, fragmentTag)
                .commit()
            fragmentStacksTags[currentStackIndex].push(fragmentTag)

            mCurrentFrag = fragment
        }
    }

    private fun FragmentTransaction.removeDuplicate(fragment: Fragment): FragmentTransaction {
        val sameFragmentScreenTags = fragmentStacksTags[currentStackIndex].filter {
            it.startsWith(fragment.generateTagPrefix())
        }
        if (sameFragmentScreenTags.isNotEmpty()) {
            val newArg = fragment.getForwardParcelable()
            for (tag in sameFragmentScreenTags) {
                val fragmentWithTag = getFragment(tag) ?: continue
                val oldArg = fragmentWithTag.getForwardParcelable()
                if (newArg == oldArg) {
                    this.removeSafe(fragmentWithTag)
                    break
                }
            }
        }
        return this
    }

    /**
     * Serves to remove the fragments until given [popDepth] from BackStack.
     * @param isReplacing: when it is true(mostly), after removal, we add previous fragment as current one,
     * It is false only for openAndReplace related commands, to avoid some issues.
     */
    fun popFragments(
        transactionOptions: NavTransactionOptions? = null,
        popDepth: Int = 1,
        arg: BaseArg? = null,
        isReplacing: Boolean = true
    ): Int {
        if (popDepth < 1) {
            throw UnsupportedOperationException("popFragments parameter needs to be greater than 0")
        } else if (currentStackIndex == NO_TAB) {
            throw UnsupportedOperationException("You can not pop fragments when no tab is selected")
        }

        // If our popDepth is big enough that it would just clear the stack, then call that.
        val currentStack = fragmentStacksTags[currentStackIndex]
        val poppableSize = currentStack.size - 1
        if (popDepth > poppableSize) {
            clearStack(currentStackIndex)
            return poppableSize
        }

        val ft = createTransaction(options = transactionOptions, isPopping = true)

        // Pop the number of the fragments on the stack and remove them from the FragmentManager
        for (i in 0 until popDepth) {
            val fragment = getFragment(currentStack.pop())
            if (fragment != null) {
                ft.removeSafe(fragment)
            }
        }

        if (isReplacing) {
            // Attempt to reattach previous fragment
            val fragment = addPreviousFragment(ft)
            ft.commit()

            mCurrentFrag = fragment
            mCurrentFrag?.arguments?.apply {
                arg?.let { putParcelable(ArgumentType.BACKWARD.key, it) }
            }
        } else {
            ft.commit()
        }
        return popDepth
    }

    private fun FragmentTransaction.detachCurrentFragment(): FragmentTransaction {
        currentFrag?.let {
            this.detach(it)
        }
        return this
    }

    /**
     * Serves to clear fragment manager
     */
    internal fun clearFragmentManager() {
        val currentFragments = fragmentManger.fragments.filterNotNull()
        if (currentFragments.isNotEmpty()) {
            with(createTransaction(isPopping = false)) {
                currentFragments.forEach { removeSafe(it) }
                commit()
            }
        }
    }

    private fun Fragment.generateTagPrefix() = this.javaClass.name

    private fun generateTag(fragment: Fragment): String {
        return fragment.generateTagPrefix() + ++tagCount
    }

    private fun Fragment?.isTheSameWith(fragment: Fragment?): Boolean {
        return this != null && fragment != null &&
                fragment.tag?.startsWith(this.generateTagPrefix()) ?: false &&
                fragment.getForwardParcelable() == this.getForwardParcelable()
    }

    private fun Fragment.getForwardParcelable() = this.arguments
        ?.getParcelable<Parcelable>(ArgumentType.FORWARD.key)

    private fun createTransaction(
        options: NavTransactionOptions? = null,
        isPopping: Boolean,
        animated: Boolean = true
    ): FragmentTransaction {
        return fragmentManger.beginTransaction().apply {
            options?.also { options ->
                // Not using standard pop support since we handle backstack manually
                if (animated) {
                    if (isPopping) {
                        setCustomAnimations(
                            options.popEnterAnimation,
                            options.popExitAnimation
                        )
                    } else {
                        setCustomAnimations(
                            options.enterAnimation,
                            options.exitAnimation
                        )
                    }
                }

                setTransitionStyle(options.transitionStyle)

                setTransition(options.transition)

                options.sharedElements.forEach { sharedElement ->
                    addSharedElement(
                        sharedElement.first,
                        sharedElement.second
                    )
                }
            }
        }
    }

    private fun getRootFragment(index: Int): Fragment {
        return rootFragmentListener.getRootFragment(index)
    }

    private fun getFragment(tag: String): Fragment? {
        val weakReference = fragmentCache[tag]
        if (weakReference != null) {
            val fragment = weakReference.get()
            if (fragment != null) {
                return fragment
            }
            fragmentCache.remove(tag)
        }
        return fragmentManger.findFragmentByTag(tag)
    }

    /**
     * Adds fragment to the fragment transaction, also add it to local cache so we can obtain it even before transaction has been committed.
     */
    private fun FragmentTransaction.addSafe(containerViewId: Int, fragment: Fragment, tag: String): FragmentTransaction {
        fragmentCache[tag] = WeakReference(fragment)
        add(containerViewId, fragment, tag)
        return this
    }

    /**
     * Remove the fragment from transaction and also from cache if found.
     */
    private fun FragmentTransaction.removeSafe(fragment: Fragment) {
        val tag = fragment.tag
        if (tag != null) {
            fragmentCache.remove(tag)
        }
        remove(fragment)
    }

    /**
     * Restores this instance to the state specified by the contents of savedInstanceState
     *
     * @param savedInstanceState The bundle to restore from
     * @return true if successful, false if not
     */
    private fun restoreFromBundle(savedInstanceState: Bundle?): Boolean {
        if (savedInstanceState == null) {
            return false
        }

        // Restore tag count
        tagCount = savedInstanceState.getInt(EXTRA_TAG_COUNT, 0)

        // Restore current fragment
        val tag = savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT)
        if (tag != null) {
            mCurrentFrag = getFragment(tag)
        }

        // Restore fragment stacks
        try {
            val stackArrays = JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK))

            for (x in 0 until stackArrays.length()) {
                val stackArray = stackArrays.getJSONArray(x)
                val stack = Stack<String>()
                (0 until stackArray.length())
                    .map { stackArray.getString(it) }
                    .filter { !it.isNullOrEmpty() && !"null".equals(it, ignoreCase = true) }
                    .mapNotNullTo(stack) { it }

                fragmentStacksTags.add(stack)
            }
            // Restore selected tab if we have one
            val selectedTabIndex = savedInstanceState.getInt(EXTRA_SELECTED_TAB_INDEX)
            if (selectedTabIndex in 0 until MAX_NUM_TABS) {
                // Shortcut for switchTab. We  already restored fragment, so just notify history controller
                // We cannot use switchTab, because switchTab removes fragment, but we don't want it
                currentStackIndex = selectedTabIndex
                switchTab(selectedTabIndex)
            }

            // Successfully restored state
            return true
        } catch (ex: Throwable) {
            tagCount = 0
            mCurrentFrag = null
            fragmentStacksTags.clear()
            Timber.e(ex, "Could not restore fragment state")
            return false
        }
    }

    /**
     * Called in Activity's onSaveInstanceState(Bundle outState) method to save the instance's state.
     *
     * @param outState The Bundle to save state information to
     */
    fun onSaveInstanceState(outState: Bundle?) {
        if (outState == null) {
            return
        }
        // Write tag count
        outState.putInt(EXTRA_TAG_COUNT, tagCount)

        // Write select tab
        outState.putInt(EXTRA_SELECTED_TAB_INDEX, currentStackIndex)

        // Write current fragment
        val currentFrag = currentFrag
        if (currentFrag != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, currentFrag.tag)
        }

        // Write tag stacks

        try {
            val stackArrays = JSONArray()
            fragmentStacksTags.forEach { stack ->
                val stackArray = JSONArray()
                stack.forEach { stackArray.put(it) }
                stackArrays.put(stackArray)
            }
            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString())
        } catch (t: Throwable) {
            Timber.e(t, "Could not save fragment stack")
            // Nothing we can do
        }
    }

    interface RootFragmentListener {
        val numberOfRootFragments: Int
        /**
         * Dynamically create the Fragment that will go on the bottom of the stack
         *
         * @param index the index that the root of the stack Fragment needs to go
         * @return the new Fragment
         */
        fun getRootFragment(index: Int): Fragment
    }

    companion object {
        // Declare the constants. A maximum of 5 tabs is recommended for bottom navigation, this is per Material Design's Bottom Navigation's design spec.
        const val NO_TAB = -1
        const val TAB1 = 0

        internal const val MAX_NUM_TABS = 20

        // Extras used to store savedInstanceState
        private val EXTRA_TAG_COUNT = NavController::class.java.name + ":EXTRA_TAG_COUNT"
        private val EXTRA_SELECTED_TAB_INDEX =
            NavController::class.java.name + ":EXTRA_SELECTED_TAB_INDEX"
        private val EXTRA_CURRENT_FRAGMENT =
            NavController::class.java.name + ":EXTRA_CURRENT_FRAGMENT"
        private val EXTRA_FRAGMENT_STACK =
            NavController::class.java.name + ":EXTRA_FRAGMENT_STACK"

    }
}