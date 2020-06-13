package com.bael.kirin

import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.IBinder
import android.os.Messenger
import android.view.Gravity.START
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import androidx.core.widget.addTextChangedListener
import com.bael.kirin.databinding.ToggleLayoutBinding
import com.bael.kirin.databinding.TranslationLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 * Created by ErickSumargo on 01/06/20.
 */

class TranslationService :
    Service(),
    CoroutineScope,
    OnKeyEventPreImeListener,
    OnDialogListener {
    private lateinit var toggleBinder: ToggleLayoutBinding
    private lateinit var translationBinder: TranslationLayoutBinding

    private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(this) }
    private val layoutManager: LayoutManager by lazy { LayoutManager(this) }

    private val messenger: Messenger by lazy { Messenger(MessengerHandler(this)) }

    override val coroutineContext: CoroutineContext get() = Main

    private var isTranslationActived: Boolean = false
    private var isDialogOpened: Boolean = false

    override fun onBind(intent: Intent): IBinder? = messenger.binder

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
                                isTranslationActived = false

                                if (isDialogOpened) {
                                    onHideSoftKeyboard()
                                } else {
                                    onDismissDialog()
                                }
                                binder.toggleIcon.setImageDrawable(appIcon.apply {
                                    setTint(GRAY_LIGHT)
                                })
                            } else {
                                isTranslationActived = true
                                addTranslationLayout()

                                binder.toggleIcon.setImageDrawable(appIcon.apply { setTint(PRIMARY) })
                            }
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
            binder.translationLayout.also { layout ->
                layout.setOnKeyPressListener(this)
            }

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
                input.setOnFocusChangeListener { _, focused ->
                    if (focused) {
                        updateTranslationLayout(FLAG_NOT_TOUCH_MODAL)
                        launch(coroutineContext) {
                            delay(100)
                            showSoftKeyboard()
                            showDialog()
                        }
                    }
                }

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

    private fun updateTranslationLayout(flag: Int) {
        layoutManager.updateLayout(
            translationBinder.translationLayout,
            translationLayoutParams.also { it.flags = flag }
        )
    }

    private fun removeTranslationLayout() {
        layoutManager.removeLayout(translationBinder.translationLayout)
    }

    override fun onHideSoftKeyboard() {
        broadcastData(Intent(SUBJECT_DISMISS_DIALOG.hashCode().toString()))
    }

    override fun onDismissDialog() {
        translationBinder.sourceLanguageInput.also { input ->
            input.clearFocus()
            hideSoftKeyboard(input)
        }

        if (isTranslationActived) {
            updateTranslationLayout(FLAG_NOT_FOCUSABLE)
        } else {
            removeTranslationLayout()
        }

        isDialogOpened = false
    }

    fun showDialog() {
        Intent(this, DialogActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also(::startActivity)
        isDialogOpened = true
    }

    fun broadcastData(intent: Intent) {
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        layoutManager.removeLayout(toggleBinder.toggleLayout)
        layoutManager.removeLayout(translationBinder.translationLayout)
        super.onDestroy()
    }
}
