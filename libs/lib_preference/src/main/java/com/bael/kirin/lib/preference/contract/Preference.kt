package com.bael.kirin.lib.preference.contract

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Preference {

    fun <T> read(key: String, defaultValue: T): T

    fun <T> write(key: String, value: T)
}
