package com.bael.kirin.feature.translation.screen.settings

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Intent {

    object ConfigSetting : Intent()

    data class ConfigSetupFailed(val message: String) : Intent()

    object ConfigSetupSuccess : Intent()

    object CheckPermissionDrawOverlays : Intent()

    object AllowPermissionDrawOverlays : Intent()

    object DenyPermissionDrawOverlays : Intent()
}
