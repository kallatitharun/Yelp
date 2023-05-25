package com.example.yelp.presentation.base.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.yelp.R
import com.example.yelp.presentation.base.navigation.*
import com.example.yelp.presentation.extensions.view.observe
import com.example.yelp.presentation.extensions.view.observeIgnoreNull
import com.example.yelp.presentation.viewModels.BaseViewModel
import com.example.yelp.presentation.extensions.view.handleToastMessageEvent
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity : AppCompatActivity(), INavigator,
    NavController.RootFragmentListener {

    protected val fragNavController: NavController = NavController(supportFragmentManager, R.id.container, this)

    private val defaultOptions = NavTransactionOptions.newBuilder().build()

    private val screenFactory: BaseScreenFactory by inject()

    open val viewModel: BaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragNavController.initialize(0, savedInstanceState)


        observeIgnoreNull(viewModel.command, ::execute)
        observe(viewModel.toastMessage, ::handleToastMessageEvent)
    }

    open fun getScreen(screen: Screen): Fragment {
        return screenFactory.getScreen(screen)
    }

    /**
     * Serves to navigate based on given command
     */
    override fun execute(command: Command) {
        try {
            when (command) {

                is Open -> fragNavController.pushFragment(
                    screenFactory.getScreen(command.screen),
                    defaultOptions
                )
                is Back -> {
                    fragNavController.popFragments(
                        transactionOptions = defaultOptions,
                        arg = command.arg
                    )
                }
                is OpenWeb -> openWeb(command.url)
                else -> throw IllegalAccessException("BottomNavigationActivity activity doesn't support command ${command.javaClass.name}")
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }


    /**
     * Provides ability to open web browser for given as param link
     *
     * @param url is a link for opening in web browser
     */
    private fun openWeb(url: String) {
        val uri = if(!url.startsWith("http")) {
            Uri.parse("https://$url")
        } else {
            Uri.parse(url)
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(browserIntent)
    }

    override fun onBackPressed() {
        // do nothing if current fragment is not allowed to back
        val currentFrag = fragNavController.currentFrag
        if (currentFrag is BackHandler) {
            currentFrag.onBack()
            return
        }

        if (!fragNavController.isLastInStack) {
            execute(Back())
        } else {
            if (fragNavController.currentStackIndex == 0) {
                finishAffinity()
            }
        }
    }
}