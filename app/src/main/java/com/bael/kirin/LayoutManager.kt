package com.bael.kirin

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams

/**
 * Created by ErickSumargo on 01/06/20.
 */

class LayoutManager(context: Context) {
    private val windowManager: WindowManager by lazy {
        context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun addLayout(view: View, layoutParams: LayoutParams) {
        if (view.parent != null) return
        windowManager.addView(view, layoutParams)
    }

    fun updateLayout(view: View, layoutParams: LayoutParams) {
        if (view.parent == null) return
        windowManager.updateViewLayout(view, layoutParams)
    }

    fun removeLayout(view: View) {
        windowManager.removeView(view)
    }
}
