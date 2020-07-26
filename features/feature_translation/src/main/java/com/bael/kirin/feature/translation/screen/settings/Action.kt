package com.bael.kirin.feature.translation.screen.settings

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Action {

    fun showSetupProgress()

    fun handleSetupFailed(message: String)

    fun onSetupSuccess()

    fun checkPermissionDrawOverlays()

    fun requestPermissionDrawOverlays()

    fun closeScreen()
}
