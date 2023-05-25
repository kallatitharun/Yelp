package com.example.yelp.presentation.delegate

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.presentation.rvAdapter.BaseHolder
import com.example.yelp.presentation.rvAdapter.BaseViewBindingDSLHolder
import com.example.yelp.presentation.rvAdapter.BaseViewBindingHolder

typealias ItemProvider<T> = () -> T?

@Keep
interface IDelegate<T : IItemType> {
    fun onCreateListeners(holder: BaseHolder, provideItemByHolder: ItemProvider<T>) {
        /*No implementation*/
    }

    fun onCreateViewHolder(parent: ViewGroup): BaseHolder
    fun getItemClass(): Class<T>
}

interface IDelegateViewBinding<T : IItemType, VB : ViewBinding> : IDelegate<T> {

    override fun onCreateListeners(holder: BaseHolder, provideItemByHolder: ItemProvider<T>) {
        super.onCreateListeners(holder, provideItemByHolder)
        (holder as BaseViewBindingHolder<T, VB>).onCreateListeners(provideItemByHolder)
    }

    fun BaseViewBindingHolder<T, VB>.onCreateListeners(provideItemByHolder: ItemProvider<T>) {
        this.binder.onCreateListeners(provideItemByHolder)
    }

    fun VB.onCreateListeners(provideItemByHolder: ItemProvider<T>) {
        /*No implementation*/
    }

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewBindingHolder<T, VB>
}

inline fun <reified T : IItemType, VB : ViewBinding> createDelegate(
    crossinline viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> VB,
    crossinline build: ViewBindingDelegateBuilder<T, VB>.() -> Unit = {}
): IDelegateViewBinding<T, VB> {
    val itemClass = T::class.java
    return object : IDelegateViewBinding<T, VB> {

        override fun BaseViewBindingHolder<T, VB>.onCreateListeners(provideItemByHolder: ItemProvider<T>) {
            // Casting is safe, because we create same class anonymously below
            val holder = (this as BaseViewBindingDSLHolder<T, VB>)
            holder.dslBuilder.applyListeners(provideItemByHolder)
        }

        override fun onCreateViewHolder(parent: ViewGroup): BaseViewBindingHolder<T, VB> {
            val inflater = LayoutInflater.from(parent.context)
            val binder = viewBindingFactory(inflater, parent, false)
            val dslBuilder = ViewBindingDelegateBuilder<T, VB>(binder).apply {
                build()
            }
            return BaseViewBindingDSLHolder(binder, dslBuilder)
        }

        override fun getItemClass(): Class<T> = itemClass
    }
}

@DslMarker
annotation class AdapterDelegateDslMarker

class ViewBindingDelegateBuilder<T : IItemType, VB : ViewBinding>(
    private val binder: VB
) {

    lateinit var holder: BaseViewBindingDSLHolder<T, VB>

    private var applyBind: VB.(T) -> Unit = {}
    private var applyBindWithPayload: VB.(List<Any>) -> Unit = {}
    private var applyListeners: VB.(ItemProvider<T>) -> Unit = {}

    val context: Context
        get() = binder.root.context

    @AdapterDelegateDslMarker
    fun bind(applyBind: VB.(T) -> Unit) {
        this.applyBind = applyBind
    }

    fun applyBind(item: T) {
        binder.applyBind(item)
    }

    fun applyBindWithPayload(payloads: List<Any>) {
        binder.applyBindWithPayload(payloads)
    }

    @AdapterDelegateDslMarker
    fun listeners(applyListeners: VB.(ItemProvider<T>) -> Unit) {
        this.applyListeners = applyListeners
    }

    fun applyListeners(p: ItemProvider<T>) {
        binder.applyListeners(p)
    }

    fun getColor(@ColorRes res: Int): Int {
        val ctx = binder.root.context
        return ResourcesCompat.getColor(ctx.resources, res, null)
    }

}

