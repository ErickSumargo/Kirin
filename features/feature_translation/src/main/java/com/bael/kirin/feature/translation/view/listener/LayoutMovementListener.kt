package com.bael.kirin.feature.translation.view.listener

import android.graphics.Point
import android.view.Display
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration.getLongPressTimeout
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import com.bael.kirin.feature.translation.util.Util.calculateDistance
import java.lang.System.currentTimeMillis

/**
 * Created by ErickSumargo on 01/06/20.
 */

class LayoutMovementListener(
    windowManager: WindowManager,
    private val layoutParams: LayoutParams,
    private val maxDiffDistance: Int = -1,
    private val onClickLayout: () -> Unit,
    private val onMoveLayout: (LayoutParams) -> Unit,
    private val onAutoAdjustPositionLayout: ((LayoutParams, Pair<Int, Int>, Pair<Int, Int>) -> Unit)? = null
) : View.OnTouchListener {
    private val display: Display = windowManager.defaultDisplay
    private val window: Point = Point()

    private var xPosition: Int = layoutParams.x
    private var yPosition: Int = layoutParams.y

    private var xFinalPosition: Int = layoutParams.x
    private var yFinalPosition: Int = layoutParams.y

    private var xTouchPosition: Float = -1.0f
    private var yTouchPosition: Float = -1.0f

    private var lastAction: Int = -1
    private var pressedDuration: Long = -1L

    init {
        display.getSize(window)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return when (motionEvent.action) {
            ACTION_DOWN -> {
                xPosition = xFinalPosition
                yPosition = yFinalPosition

                xTouchPosition = motionEvent.rawX
                yTouchPosition = motionEvent.rawY

                lastAction = motionEvent.action
                pressedDuration = currentTimeMillis()

                true
            }
            ACTION_UP -> {
                when (lastAction) {
                    ACTION_DOWN -> onClickLayout()
                    ACTION_MOVE -> {
                        if (currentTimeMillis() - pressedDuration < getLongPressTimeout() &&
                            calculateDistance(
                                coordsStart = xPosition to yPosition,
                                coordsEnd = xFinalPosition to yFinalPosition
                            ) < maxDiffDistance
                        ) onClickLayout()
                    }
                }
                lastAction = motionEvent.action

                onAutoAdjustPositionLayout?.let { callback ->
                    val xNewFinalPosition = 0.takeIf { xFinalPosition < window.x / 2 } ?: window.x
                    callback(
                        layoutParams,
                        xFinalPosition to yFinalPosition,
                        xNewFinalPosition to yFinalPosition
                    )
                    xFinalPosition = xNewFinalPosition
                }
                true
            }
            ACTION_MOVE -> {
                xFinalPosition = xPosition + (motionEvent.rawX - xTouchPosition).toInt()
                yFinalPosition = yPosition + (motionEvent.rawY - yTouchPosition).toInt()

                lastAction = motionEvent.action
                onMoveLayout(
                    layoutParams.also { params ->
                        params.x = xFinalPosition
                        params.y = yFinalPosition
                    }
                )
                true
            }
            else -> false
        }
    }
}
