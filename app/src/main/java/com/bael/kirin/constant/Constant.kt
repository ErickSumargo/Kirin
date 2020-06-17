package com.bael.kirin.constant

import android.graphics.PixelFormat.TRANSLUCENT
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_PHONE

/**
 * Created by ErickSumargo on 01/06/20.
 */

const val SUBJECT_DISMISS_DIALOG = 100

val toggleLayoutParams: LayoutParams =
    if (SDK_INT >= O) {
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_APPLICATION_OVERLAY,
            FLAG_NOT_FOCUSABLE,
            TRANSLUCENT
        )
    } else {
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_PHONE,
            FLAG_NOT_FOCUSABLE,
            TRANSLUCENT
        )
    }

val translationLayoutParams: LayoutParams =
    if (SDK_INT >= O) {
        LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT,
            TYPE_APPLICATION_OVERLAY,
            FLAG_NOT_FOCUSABLE,
            TRANSLUCENT
        )
    } else {
        LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT,
            TYPE_PHONE,
            FLAG_NOT_FOCUSABLE,
            TRANSLUCENT
        )
    }
