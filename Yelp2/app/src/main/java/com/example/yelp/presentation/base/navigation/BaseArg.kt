package com.example.yelp.presentation.base.navigation

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.yelp.domain.navModels.BaseArg
import kotlinx.parcelize.Parcelize

enum class ArgumentType(val key: String) {
    FORWARD("forward_argument"), BACKWARD("backward_argument");
}

@Parcelize
data class ToastMessageArg(@StringRes val value: Int) : BaseArg

/**
 * Creates new fragment with argument if provided
 */
inline fun <reified T : Fragment> newFragment(param: BaseArg? = null): T {
    val fragment = T::class.java.newInstance()
    fragment.arguments = Bundle().apply {
        param?.let { putParcelable(ArgumentType.FORWARD.key, it) }
    }
    return fragment
}

/**
 * Method provide base argument if exists either null
 */
inline fun <reified T : BaseArg> Fragment.argValue(argType: ArgumentType): T? {
    return arguments?.getParcelable(argType.key)
}

/**
 * Method provide base argument if exists either null
 */
inline fun <reified T : BaseArg> Fragment.obtainArgAndClear(argType: ArgumentType): T? {
    return arguments?.let {
        val value = it.getParcelable<T>(argType.key)
        it.remove(argType.key)
        return@let value
    }
}