package com.example.yelp.presentation.features.businessInformation.businessDetails

import android.text.Html
import android.text.method.LinkMovementMethod
import com.example.yelp.databinding.ItemBusinessDetailsBinding
import com.example.yelp.domain.useCases.BusinessDetailsItem
import com.example.yelp.presentation.delegate.createDelegate

fun DetailsDelegate(onButtonCLick: (BusinessDetailsItem) -> Unit) = createDelegate<BusinessDetailsItem, ItemBusinessDetailsBinding>(ItemBusinessDetailsBinding::inflate) {
    bind {
        addressValue.text = it.address
        phoneValue.text = it.phone
        categoryValue.text = it.category
        priceRangeValue.text = it.priceRange
        statusValue.text = it.status
        statusValue.setTextColor(getColor(it.statusColor))
        val text = "<a href='${it.link}'> Business link </a>"
        visitValue.isClickable = true
        visitValue.movementMethod = LinkMovementMethod.getInstance()
        visitValue.text = Html.fromHtml(text)
    }

    listeners { provider ->
        reserveButton.setOnClickListener {
            provider()?.let { item ->
                onButtonCLick(item)
            }
        }
    }
}