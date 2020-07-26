package com.bael.kirin.lib.resource.util

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.N
import android.os.Build.VERSION_CODES.O

/**
 * Created by ErickSumargo on 01/06/20.
 */

object Util {
    val minMarshmallowSdk: Boolean
        get() = SDK_INT >= M
    val minNougatSdk: Boolean
        get() = SDK_INT >= N
    val minOreoSdk: Boolean
        get() = SDK_INT >= O
}
