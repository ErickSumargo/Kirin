package com.bael.kirin.lib.tracker.contract

import android.os.Bundle

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Tracker {

    fun track(
        event: String,
        data: Bundle = Bundle()
    )
}
