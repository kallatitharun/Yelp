package com.example.yelp.presentation.rvAdapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemClickListener
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.domain.base.rvModels.PayloadAware
import com.example.yelp.presentation.delegate.IDelegate
import kotlinx.coroutines.*

@Keep
abstract class BaseAdapter : RecyclerView.Adapter<BaseHolder>() {
    protected val delegates: SparseArray<IDelegate<in IItemType>> = SparseArray()
    var itemClickListener: IItemClickListener? = null
    protected val itemsList: MutableList<IItemType> = ArrayList()

    private var job: Job = Job()
    private var dataVersion = 0
    private val scope = CoroutineScope(Dispatchers.Main + job)

    /**
     * Remove given item from list of items. Nothing happens if there is no given item.
     *
     * @param item is item to remove
     */
    internal fun removeItem(item: IItemType) {
        val index = itemsList.indexOf(item)
        if (index == -1) return

        itemsList.remove(item)
        notifyItemRemoved(index)
    }

    fun getItem(position: Int) = itemsList[position]

    fun clear() {
        replace(null)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int = getItemViewType(itemsList, position)

    /**
     * Serves to generate item view type by item's class to current position
     * It also supports search for interfaces if we use them on delegate level
     */
    @Suppress("UNCHECKED_CAST")
    protected fun getItemViewType(list: List<IItemType>, position: Int): Int {
        val clazz: Class<out IItemType?> = list[position].javaClass
        val uniqueItemCode = getUniqueItemCode(clazz)
        val indexOfKey = delegates.indexOfKey(uniqueItemCode)
        if (indexOfKey < 0) {
            val interfaces = clazz.interfaces
            for (i in interfaces) {
                val interfaceClazz = i as Class<out IItemType?>?
                if (interfaceClazz != null) {
                    val code = getUniqueItemCode(interfaceClazz)
                    if (delegates.indexOfKey(code) >= 0) {
                        return code
                    }
                }
            }
        }
        return uniqueItemCode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val iDelegate = delegates.get(viewType)
        val holder = iDelegate.onCreateViewHolder(parent)
        iDelegate.onCreateListeners(holder) {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                return@onCreateListeners itemsList[holder.adapterPosition]
            }
            return@onCreateListeners null
        }
        if (itemClickListener != null) {
            holder.setOnItemClickListener(itemClickListener as IItemClickListener)
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) =
        holder.bind(itemsList[position], position)

    override fun onBindViewHolder(holder: BaseHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.onBindWithPayloads(payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getUniqueItemCode(clazzOfItemType: Class<out IItemType>): Int {
        return clazzOfItemType.canonicalName.hashCode()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        job = Job()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        job.cancel()
    }

    override fun onViewDetachedFromWindow(holder: BaseHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detach()
    }

    override fun onViewAttachedToWindow(holder: BaseHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    fun replace(updateItems: List<IItemType>?) {
        dataVersion++
        val oldSize = itemsList.size
        if (oldSize == 0) {
            if (updateItems.isNullOrEmpty()) {
                return
            }
            itemsList.addAll(updateItems)
            notifyDataSetChanged()
        } else if (updateItems.isNullOrEmpty()) {
            itemsList.clear()
            notifyItemRangeRemoved(0, oldSize)
        } else {
            val startVersion = dataVersion
            val constOldItems = itemsList.toList()
            val constUpdateItems = updateItems.toList()

            scope.launch {
                val diffResult = calculateDiff(constOldItems, constUpdateItems)
                if (startVersion != dataVersion) {
                    // ignore update
                    return@launch
                }
                itemsList.clear()
                itemsList.addAll(constUpdateItems)

                diffResult.dispatchUpdatesTo(this@BaseAdapter)
            }
        }
    }

    private fun areTypesMatch(
        oldItems: List<IItemType>,
        updateItems: List<IItemType>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return getItemViewType(oldItems, oldItemPosition) !=
                getItemViewType(updateItems, newItemPosition)
    }

    private suspend fun calculateDiff(oldItems: List<IItemType>, updateItems: List<IItemType>) =
        withContext(Dispatchers.Default) {
            return@withContext DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    if (areTypesMatch(oldItems, updateItems, oldItemPosition, newItemPosition)) {
                        return false
                    }
                    val oldItem = oldItems[oldItemPosition]
                    val newItem = updateItems[newItemPosition]
                    if (oldItem is Diffable) {
                        return oldItem.areItemsTheSame(newItem)
                    }
                    return false
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    val oldItem = oldItems[oldItemPosition]
                    val newItem = updateItems[newItemPosition]
                    if(oldItem is PayloadAware && newItem is PayloadAware) {
                        return oldItem.getPayload(newItem)
                    }
                    return super.getChangePayload(oldItemPosition, newItemPosition)
                }

                override fun getOldListSize() = oldItems.size
                override fun getNewListSize() = updateItems.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    // Here we 100% sure that
                    // 1. items have same type
                    // 2. same unique identifier
                    // 3. is Diffable
                    return oldItems[oldItemPosition] == updateItems[newItemPosition]
                }
            })
        }
}