package com.bael.kirin

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import androidx.cardview.widget.CardView

/**
 * Created by ErickSumargo on 01/06/20.
 */

class KeyEventPreImeDispatcherCardView : CardView {
    private var onKeyEventListener: OnKeyEventPreImeListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setOnKeyPressListener(onKeyEventListener: OnKeyEventPreImeListener) {
        this.onKeyEventListener = onKeyEventListener
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        when (event?.action) {
            ACTION_DOWN -> onKeyEventListener?.onHideSoftKeyboard()
        }
        return super.dispatchKeyEventPreIme(event)
    }
}
