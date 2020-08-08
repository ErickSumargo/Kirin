package com.bael.kirin.feature.translation.screen.background

import com.bael.kirin.lib.base.intent.BaseIntent

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Intent : BaseIntent() {

    object Initialize : Intent()

    data class InstantTranslate(val query: String) : Intent()

    data class StartService(val query: String) : Intent()

    data class OpenMainScreen(val query: String) : Intent()
}
