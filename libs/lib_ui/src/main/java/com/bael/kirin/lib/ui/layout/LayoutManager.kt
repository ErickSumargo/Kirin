package com.bael.kirin.lib.ui.layout

import android.animation.PropertyValuesHolder.ofInt
import android.animation.ValueAnimator.ofPropertyValuesHolder
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.view.animation.AccelerateInterpolator
import com.bael.kirin.lib.logger.contract.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class LayoutManager @Inject constructor(
    @ApplicationContext context: Context,
    logger: Logger
) : Logger by logger {
    val windowManager: WindowManager by lazy {
        context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun addLayout(
        view: View,
        layoutParams: LayoutParams
    ) {
        try {
            if (view.parent != null) return
            windowManager.addView(view, layoutParams)
        } catch (cause: Exception) {
            log(cause)
        }
    }

    fun updateLayout(
        view: View,
        layoutParams: LayoutParams
    ) {
        try {
            if (view.parent == null) return
            windowManager.updateViewLayout(view, layoutParams)
        } catch (cause: Exception) {
            log(cause)
        }
    }

    fun adjustPositionLayout(
        view: View,
        layoutParams: LayoutParams,
        coordsStart: Pair<Int, Int>,
        coordsEnd: Pair<Int, Int>
    ) {
        try {
            if (view.parent == null) return

            val (xStart, yStart) = coordsStart
            val (xEnd, yEnd) = coordsEnd
            ofPropertyValuesHolder(
                ofInt("x", xStart, xEnd),
                ofInt("y", yStart, yEnd)
            ).apply {
                interpolator = AccelerateInterpolator()
                duration = 300

                addUpdateListener { value ->
                    layoutParams.also { params ->
                        params.x = value.getAnimatedValue("x") as Int
                        params.y = value.getAnimatedValue("y") as Int

                        windowManager.updateViewLayout(view, params)
                    }
                }
            }.also { it.start() }
        } catch (cause: Exception) {
            log(cause)
        }
    }

    fun removeLayout(view: View) {
        try {
            windowManager.removeView(view)
        } catch (cause: Exception) {
            log(cause)
        }
    }
}
