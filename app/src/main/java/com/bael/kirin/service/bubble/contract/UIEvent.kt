package com.bael.kirin.service.bubble.contract

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface UIEvent {
    fun showToggleLayout()
    fun showTranslationLayout()
    fun hideTranslationLayout()
    fun showDialog()
    fun dismissDialog()
    fun showSoftKeyboard()
    fun hideSoftKeyboard()
}
