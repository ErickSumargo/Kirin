package com.bael.kirin

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.WindowManager.LayoutParams

/**
 * Created by ErickSumargo on 01/06/20.
 */

class LayoutMovementListener constructor(
    private val layoutParams: LayoutParams,
    private val onClickLayout: () -> Unit,
    private val onMoveLayout: (LayoutParams) -> Unit
) : View.OnTouchListener {
    private var xPosition: Int = layoutParams.x
    private var yPosition: Int = layoutParams.y

    private var xFinalPosition: Int = layoutParams.x
    private var yFinalPosition: Int = layoutParams.y

    private var xTouchPosition: Float = -1.0f
    private var yTouchPosition: Float = -1.0f

    private var lastAction: Int = -1

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return when (motionEvent.action) {
            ACTION_DOWN -> {
                xPosition = xFinalPosition
                yPosition = yFinalPosition

                xTouchPosition = motionEvent.rawX
                yTouchPosition = motionEvent.rawY

                lastAction = motionEvent.action
                true
            }
            ACTION_UP -> {
                if (lastAction == ACTION_DOWN) {
                    onClickLayout()
                }
                lastAction = motionEvent.action
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
