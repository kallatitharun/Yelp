package com.example.yelp.presentation.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * Serves to clean code. Use it to implement TextWatcher and override only necessary callback.
 */
open class EmptyTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {}
}