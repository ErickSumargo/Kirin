package com.bael.kirin.ext

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

/**
 * Created by ErickSumargo on 01/06/20.
 */

fun Context.drawable(@DrawableRes resource: Int): Drawable {
    val drawable = AppCompatResources.getDrawable(this, resource)
    return drawable ?: ColorDrawable()
}
