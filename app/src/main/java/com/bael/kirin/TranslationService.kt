package com.bael.kirin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.Gravity.START
import android.view.LayoutInflater
import com.bael.kirin.databinding.ToggleLayoutBinding

/**
 * Created by ErickSumargo on 01/06/20.
 */

class TranslationService :
    Service() {
    private lateinit var toggleBinder: ToggleLayoutBinding

    private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(this) }
    private val layoutManager: LayoutManager by lazy { LayoutManager(this) }

    private var isTranslationActived: Boolean = false

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        initLayout()
        addToggleLayout()
    }

    private fun initLayout() {
        toggleBinder = ToggleLayoutBinding.inflate(layoutInflater).also { binder ->
            binder.toggleLayout.also { layout ->
                layout.setOnTouchListener(
                    LayoutMovementListener(
                        layoutParams = toggleLayoutParams,
                        onClickLayout = {
                            if (isTranslationActived) {
                                binder.toggleIcon.setImageDrawable(appIcon.apply { setTint(GRAY_LIGHT) })
                            } else {
                                binder.toggleIcon.setImageDrawable(appIcon.apply { setTint(PRIMARY) })
                            }
                            isTranslationActived = !isTranslationActived
                        },
                        onMoveLayout = { params ->
                            layoutManager.updateLayout(layout, params)
                        }
                    )
                )
            }

            binder.toggleIcon.also { icon ->
                icon.setImageDrawable(appIcon.apply { setTint(GRAY_LIGHT) })
            }
        }
    }

    private fun addToggleLayout() {
        layoutManager.addLayout(
            toggleBinder.toggleLayout,
            toggleLayoutParams.also { it.gravity = START }
        )
    }

    override fun onDestroy() {
        layoutManager.removeLayout(toggleBinder.toggleLayout)
        super.onDestroy()
    }
}
