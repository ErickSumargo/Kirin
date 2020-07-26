package com.bael.kirin.feature.translation.screen.background

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Intent {

    object Initialize : Intent()

    data class InstantTranslate(val query: String) : Intent()

    data class StartService(val query: String) : Intent()

    data class OpenMainScreen(val query: String) : Intent()
}
