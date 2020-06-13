package com.bael.kirin

import android.content.Context
import androidx.core.content.ContextCompat.getColor

/**
 * Created by ErickSumargo on 01/06/20.
 */

val Context.PRIMARY: Int get() = getColor(this, R.color.colorPrimary)
val Context.GRAY_LIGHT: Int get() = getColor(this, R.color.grayLight)
