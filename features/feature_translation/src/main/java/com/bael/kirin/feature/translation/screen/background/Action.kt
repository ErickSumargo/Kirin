package com.bael.kirin.feature.translation.screen.background

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Action {

    fun init()

    fun instantTranslate(query: String)

    fun startService(query: String)

    fun openMainScreen(query: String)

    fun dismissBackground()
}
