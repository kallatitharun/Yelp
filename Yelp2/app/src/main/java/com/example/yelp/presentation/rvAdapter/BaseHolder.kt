package com.example.yelp.presentation.rvAdapter

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.yelp.domain.base.rvModels.IItemClickListener
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.presentation.delegate.ViewBindingDelegateBuilder

@Keep
abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
    protected lateinit var data: IItemType
    protected var itemPosition: Int = -1

    @CallSuper
    open fun bind(item: IItemType, position: Int) {
        itemPosition = position
        bind(item)
    }

    @CallSuper
    open fun bind(item: IItemType) {
        data = item
    }

    open fun detach() {}

    open fun attach() {}

    open fun onBindWithPayloads(payloads: List<Any>) {}

    fun setOnItemClickListener(listener: IItemClickListener) {
        itemView.setOnClickListener { listener.onItemClicked(data) }
    }
}

abstract class BaseViewBindingHolder<T: IItemType, VB : ViewBinding>(val binder: VB) : BaseHolder(binder.root) {

    override fun bind(item: IItemType) {
        super.bind(item)
        binder.bind(item as T, binder.root.context)
    }

    abstract fun VB.bind(item: T, context: Context)

    fun getString(@StringRes res: Int) = binder.root.context.getString(res)

    override fun onBindWithPayloads(payloads: List<Any>) {
        super.onBindWithPayloads(payloads)
        binder.onBindWithPayloads(payloads)
    }

    open fun VB.onBindWithPayloads(payloads: List<Any>) {}

    override fun detach() {
        binder.detach()
    }

    override fun attach() {
        binder.attach()
    }

    open fun VB.detach() {}

    open fun VB.attach() {}
}

class BaseViewBindingDSLHolder<T: IItemType, VB : ViewBinding>(
    binder: VB,
    val dslBuilder: ViewBindingDelegateBuilder<T, VB>
) : BaseViewBindingHolder<T, VB>(binder) {

    init {
        dslBuilder.holder = this
    }

    override fun VB.bind(item: T, context: Context) {
        dslBuilder.applyBind(item)
    }

    override fun VB.onBindWithPayloads(payloads: List<Any>) {
        dslBuilder.applyBindWithPayload(payloads)
    }
}