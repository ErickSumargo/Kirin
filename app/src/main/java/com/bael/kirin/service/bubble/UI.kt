package com.bael.kirin.service.bubble

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
import com.bael.kirin.DialogActivity
import com.bael.kirin.constant.GRAY_LIGHT
import com.bael.kirin.LayoutManager
import com.bael.kirin.LayoutMovementListener
import com.bael.kirin.MessengerHandler
import com.bael.kirin.OnDialogListener
import com.bael.kirin.OnKeyEventPreImeListener
import com.bael.kirin.constant.PRIMARY
import com.bael.kirin.constant.SUBJECT_DISMISS_DIALOG
import com.bael.kirin.constant.appIcon
import com.bael.kirin.base.BaseService
import com.bael.kirin.databinding.ToggleLayoutBinding
import com.bael.kirin.databinding.TranslationLayoutBinding
import com.bael.kirin.ext.hideSoftKeyboard
import com.bael.kirin.ext.showSoftKeyboard
import com.bael.kirin.service.bubble.contract.UIEvent
import com.bael.kirin.service.bubble.contract.UIRenderer
import com.bael.kirin.constant.toggleLayoutParams
import com.bael.kirin.constant.translationLayoutParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class UI :
    BaseService(),
    UIRenderer,
    UIEvent,
    OnKeyEventPreImeListener,
    OnDialogListener {
    private val toggleBinder: ToggleLayoutBinding by lazy {
        ToggleLayoutBinding.inflate(layoutInflater)
    }
    private val translationBinder: TranslationLayoutBinding by lazy {
        TranslationLayoutBinding.inflate(layoutInflater)
    }

    private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(this) }
    private val layoutManager: LayoutManager by lazy { LayoutManager(this) }
    private val messenger: Messenger by lazy { Messenger(MessengerHandler(this)) }

    private val viewModel: ViewModel = ViewModel(UIState())
    private val dispatcher: Dispatcher = Dispatcher(viewModel, this, this)

    override fun onBind(intent: Intent): IBinder? = messenger.binder

    override fun onCreate() {
        super.onCreate()
        dispatcher.observe()
    }

    override fun renderToggleLayout(active: Boolean) {
        toggleBinder.toggleLayout.also { layout ->
            layout.setOnTouchListener(
                LayoutMovementListener(
                    layoutParams = toggleLayoutParams,
                    onClickLayout = { viewModel.setToggleActivation(!active) },
                    onMoveLayout = { params -> layoutManager.updateLayout(layout, params) }
                )
            )
        }

        toggleBinder.toggleIcon.also { icon ->
            icon.setImageDrawable(appIcon.apply {
                setTint(PRIMARY.takeIf { active } ?: GRAY_LIGHT)
            })
        }
    }

    override fun renderTranslationLayout() {
        translationBinder.translationLayout.also { layout ->
            layout.setOnKeyPressListener(this)
        }
    }

    override fun renderSourceLanguageLabel() {
        translationBinder.sourceLanguageLabel.also { label ->
            label.text = "English"
        }
    }

    override fun renderSwapLanguageIcon() {
        translationBinder.swapLanguageIcon.also { icon ->
            icon.setOnClickListener {}
        }
    }

    override fun renderTargetLanguageLabel() {
        translationBinder.targetLanguageLabel.also { label ->
            label.text = "Indonesia"
        }
    }

    override fun renderTranslationInput(reset: Boolean) {
        translationBinder.translationInput.also { input ->
            if (reset) input.setText("")

            input.setOnFocusChangeListener { _, focused ->
                if (focused) {
                    viewModel.startEditing()
                }
            }

            input.addTextChangedListener { text ->
                viewModel.setTranslationText(text.toString())
            }
        }
    }

    override fun renderClearTextIcon(text: String) {
        translationBinder.clearTextIcon.also { icon ->
            icon.visibility = VISIBLE.takeIf { text.isNotEmpty() } ?: INVISIBLE

            icon.setOnClickListener {
                viewModel.clearTranslationText()
            }
        }
    }

    override fun renderTranslatedLabel() {
        translationBinder.targetLanguageInput.also { input ->
            input.hint = "Terjemahan"
        }
    }

    override fun renderSwipeLayout() {
        translationBinder.swipeLayout.also { layout ->
            layout.setOnTouchListener(
                LayoutMovementListener(
                    layoutParams = translationLayoutParams,
                    onClickLayout = {},
                    onMoveLayout = { params ->
                        layoutManager.updateLayout(translationBinder.translationLayout, params)
                    }
                )
            )
        }
    }

    override fun showToggleLayout() {
        layoutManager.addLayout(
            toggleBinder.toggleLayout,
            toggleLayoutParams.also { it.gravity = START }
        )
    }

    override fun showTranslationLayout() {
        layoutManager.addLayout(
            translationBinder.translationLayout,
            translationLayoutParams.also { it.gravity = START or TOP }
        )
        translationBinder.translationInput.requestFocus()
    }

    private fun updateTranslationLayout(flag: Int) {
        layoutManager.updateLayout(
            translationBinder.translationLayout,
            translationLayoutParams.also { it.flags = flag }
        )
    }

    override fun hideTranslationLayout() {
        layoutManager.removeLayout(translationBinder.translationLayout)
    }

    override fun showDialog() {
        Intent(this, DialogActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also(::startActivity)
    }

    override fun dismissDialog() {
        broadcastData(Intent(SUBJECT_DISMISS_DIALOG.hashCode().toString()))
    }

    override fun showSoftKeyboard() {
        updateTranslationLayout(FLAG_NOT_TOUCH_MODAL)
        translationBinder.translationInput.also { input ->
            launch(coroutineContext) {
                delay(100)
                input.showSoftKeyboard(this@UI)
            }
        }
    }

    override fun hideSoftKeyboard() {
        translationBinder.translationInput.also { input ->
            input.clearFocus()
            input.hideSoftKeyboard(this)
        }
        updateTranslationLayout(FLAG_NOT_FOCUSABLE)
    }

    override fun onDialogDismissed() {
        viewModel.stopEditing()
    }

    override fun onHideSoftKeyboard() {
        viewModel.stopEditing()
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
