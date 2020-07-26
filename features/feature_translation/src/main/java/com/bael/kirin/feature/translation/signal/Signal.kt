package com.bael.kirin.feature.translation.signal

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Signal {

    object BackgroundShown : Signal()

    object DismissBackground : Signal()

    object BackgroundDismissed : Signal()

    object StopService : Signal()

    data class InstantTranslate(val query: String) : Signal()
}
