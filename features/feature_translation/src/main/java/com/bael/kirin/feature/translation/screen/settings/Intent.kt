package com.bael.kirin.feature.translation.screen.settings

import com.bael.kirin.lib.arch.base.BaseIntent

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Intent : BaseIntent() {

    object ConfigSetting : Intent()

    data class ConfigSetupFailed(val message: String) : Intent()

    object ConfigSetupSuccess : Intent()

    object CheckPermissionDrawOverlays : Intent()

    object AllowPermissionDrawOverlays : Intent()

    object DenyPermissionDrawOverlays : Intent()
}
