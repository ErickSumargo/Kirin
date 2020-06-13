package com.bael.kirin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.Gravity.START
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import com.bael.kirin.databinding.ToggleLayoutBinding
import com.bael.kirin.databinding.TranslationLayoutBinding

/**
 * Created by ErickSumargo on 01/06/20.
 */

class TranslationService :
    Service() {
    private lateinit var toggleBinder: ToggleLayoutBinding
    private lateinit var translationBinder: TranslationLayoutBinding

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
                                binder.toggleIcon.setImageDrawable(appIcon.apply {
                                    setTint(
                                        GRAY_LIGHT
                                    )
                                })
                                removeTranslationLayout()
                            } else {
                                addTranslationLayout()
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

        translationBinder = TranslationLayoutBinding.inflate(layoutInflater).also { binder ->
            binder.sourceLanguageLabel.also { label ->
                label.text = "English"
            }

            binder.swapIcon.also { icon ->
                icon.setOnClickListener {}
            }

            binder.targetLanguageLabel.also { label ->
                label.text = "Indonesia"
            }

            binder.sourceLanguageInput.also { input ->
                input.addTextChangedListener { editable ->
                    translationBinder.clearIcon.visibility =
                        VISIBLE.takeIf { editable.isNullOrEmpty().not() } ?: INVISIBLE
                }
            }

            binder.clearIcon.also { icon ->
                icon.setOnClickListener {
                    translationBinder.sourceLanguageInput.text.clear()
                }
            }

            binder.targetLanguageInput.also { input ->
                input.hint = "Terjemahan"
            }

            binder.swipeLayout.also { layout ->
                layout.setOnTouchListener(
                    LayoutMovementListener(
                        layoutParams = translationLayoutParams,
                        onClickLayout = {},
                        onMoveLayout = { params ->
                            layoutManager.updateLayout(binder.translationLayout, params)
                        }
                    )
                )
            }
        }
    }

    private fun addToggleLayout() {
        layoutManager.addLayout(
            toggleBinder.toggleLayout,
            toggleLayoutParams.also { it.gravity = START }
        )
    }

    private fun addTranslationLayout() {
        layoutManager.addLayout(
            translationBinder.translationLayout,
            translationLayoutParams.also { it.gravity = START or TOP }
        )
        translationBinder.sourceLanguageInput.requestFocus()
    }

    private fun removeTranslationLayout() {
        layoutManager.removeLayout(translationBinder.translationLayout)
    }

    override fun onDestroy() {
        layoutManager.removeLayout(toggleBinder.toggleLayout)
        layoutManager.removeLayout(translationBinder.translationLayout)
        super.onDestroy()
    }
}
