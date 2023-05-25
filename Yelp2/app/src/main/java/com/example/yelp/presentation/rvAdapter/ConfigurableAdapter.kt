package com.example.yelp.presentation.rvAdapter

import android.util.SparseArray
import androidx.annotation.Keep
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.presentation.delegate.IDelegate

@Keep
open class ConfigurableAdapter : BaseAdapter() {

    /**
     * Serves to add new delegate adapter to ConfigurableAdapter
     */
    open fun <T : IDelegate<out IItemType>> addDelegateType(delegate: T): ConfigurableAdapter {
        val clazzOfItemType = delegate.getItemClass()
        val uniqueItemCode = getUniqueItemCode(clazzOfItemType)
        delegates.putStrict(uniqueItemCode, delegate)
        return this
    }
}

fun <E> SparseArray<E>.putStrict(uniqueItemCode: Int, delegate: IDelegate<out IItemType>) {
    this.put(uniqueItemCode, (delegate as E))
}
