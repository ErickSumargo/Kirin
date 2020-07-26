package com.bael.kirin.lib.tracker.contract

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Tracker {

    fun <T> track(event: String, value: T)

    fun trackIncrement(event: String)
}
