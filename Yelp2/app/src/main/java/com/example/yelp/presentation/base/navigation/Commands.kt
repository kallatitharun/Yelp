package com.example.yelp.presentation.base.navigation

import com.example.yelp.domain.navModels.BaseArg

interface Command

abstract class ScreenCommand(val screen: Screen) : Command

class Back(val arg: BaseArg? = null) : Command

/**
 * Open given screen
 * @param screen has to be Fragment
 */
class Open(screen: Screen) : ScreenCommand(screen)

/**
 * Provides ability to open web browser for given link.
 *
 * @param screen is a instance of [WebScreen], put link into constructor for [WebScreen.url] field
 */
class OpenWeb(val url: String) : Command
