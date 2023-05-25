package com.example.yelp.domain.base.rvModels

import androidx.annotation.Keep

@Keep
interface Diffable {
    fun areItemsTheSame(other: IItemType): Boolean
}