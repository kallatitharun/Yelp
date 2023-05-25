package com.example.yelp.presentation.base.navigation

import com.example.yelp.domain.navModels.BaseArg
import kotlinx.parcelize.Parcelize

abstract class Screen(val param: BaseArg? = null)

class HomeScreen : Screen()
class BusinessInfoScreen(arg: BusinessInfoArg) : Screen(arg)
class ReservationsScreen: Screen()
class SplashScreen: Screen()

@Parcelize
data class BusinessInfoArg(
    val businessId: String,
    val businessName: String,
    val websiteUrl: String = ""
) : BaseArg


