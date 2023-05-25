package com.example.yelp.domain.base.rvModels

import androidx.annotation.Keep

@Keep
/**
 * Delegate items could extend this interface to mark themselves as aware of payload changes
 * getPayload should compare two objects and return any object which describes particular changes
 * or null if we want to change this item in standard way (notifyItemChanged())
 */
interface PayloadAware {
    fun getPayload(newItem: PayloadAware): Any?
}