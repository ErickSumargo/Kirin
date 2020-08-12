package com.bael.kirin.feature.translation.service.floating

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat.TRANSLUCENT
import android.text.InputType.TYPE_CLASS_TEXT
import android.view.Gravity.START
import android.view.Gravity.TOP
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import com.bael.kirin.feature.translation.R
import com.bael.kirin.feature.translation.constant.LANGUAGE_AUTO
import com.bael.kirin.feature.translation.constant.SUBJECT_DISMISS_BACKGROUND
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_QUERY
import com.bael.kirin.feature.translation.constant.languages
import com.bael.kirin.feature.translation.databinding.ToggleLayoutBinding
import com.bael.kirin.feature.translation.databinding.TranslationLayoutBinding
import com.bael.kirin.feature.translation.ext.addQueryActionListener
import com.bael.kirin.feature.translation.ext.addQueryChangedListener
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.feature.translation.tracker.Tracker
import com.bael.kirin.feature.translation.util.Util.retrieveDeeplink
import com.bael.kirin.feature.translation.view.listener.LayoutMovementListener
import com.bael.kirin.feature.translation.view.listener.OnKeyEventPreImeListener
import com.bael.kirin.feature.translation.view.listener.SpinnerItemSelectedListener
import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.base.service.BaseService
import com.bael.kirin.lib.data.model.Data
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.resource.util.Util.minOreoSdk
import com.bael.kirin.lib.ui.constant.appIcon
import com.bael.kirin.lib.ui.constant.cerise
import com.bael.kirin.lib.ui.constant.gray
import com.bael.kirin.lib.ui.ext.hideSoftKeyboard
import com.bael.kirin.lib.ui.ext.showSoftKeyboard
import com.bael.kirin.lib.ui.layout.LayoutManager
import com.bael.kirin.lib.ui.notification.NotificationFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.bael.kirin.feature.translation.screen.background.UI as UIBackground

/**
 * Created by ErickSumargo on 01/06/20.
 */

@AndroidEntryPoint
@FlowPreview
@ExperimentalCoroutinesApi
class UI :
    BaseService(),
    Renderer,
    Action,
    OnKeyEventPreImeListener {
    @Inject
    lateinit var appInfo: AppInfo

    @Inject
    lateinit var layoutManager: LayoutManager

    @Inject
    lateinit var toggleBinder: ToggleLayoutBinding

    @Inject
    lateinit var translationBinder: TranslationLayoutBinding

    @Inject
    lateinit var viewModel: ViewModel

    @Inject
    lateinit var notification: NotificationFactory

    @Inject
    lateinit var preference: Preference

    @Inject
    lateinit var tracker: Tracker

    lateinit var packet: Packet

    lateinit var dispatcher: Dispatcher

    override fun onCreate() {
        super.onCreate()
        dispatcher = Dispatcher(
            context = this,
            viewModel = viewModel,
            renderer = this,
            action = this
        ).also { it.observe(lifecycleOwner = this) }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        val query = intent?.getStringExtra(SUBJECT_EXTRA_QUERY)
        packet = Packet(instantQuery = query)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun renderToggleLayout(active: Boolean) = launch {
        toggleBinder.toggleLayout.also { layout ->
            layout.setOnTouchListener(
                LayoutMovementListener(
                    windowManager = layoutManager.windowManager,
                    layoutParams = toggleLayoutParams,
                    maxDiffDistance = MAX_FLOATING_DIFF_DISTANCE,
                    onClickLayout = {
                        viewModel.setToggleActivation(
                            active = !active,
                            editMode = preference.useAutoEditingMode,
                            autoClearHistory = preference.autoClearHistory
                        )
                        tracker.trackToggleActivation(!active)
                    },
                    onMoveLayout = { params ->
                        layoutManager.updateLayout(layout, params)
                    },
                    onAutoAdjustPositionLayout = { params, coordsStart, coordsEnd ->
                        layoutManager.adjustPositionLayout(
                            layout,
                            params,
                            coordsStart,
                            coordsEnd
                        )
                    }
                )
            )
        }

        toggleBinder.toggleIcon.also { icon ->
            icon.setImageDrawable(appIcon.apply {
                setTint(cerise.takeIf { active } ?: gray)
            })
        }
    }

    override fun renderTranslationLayout() = launch {
        translationBinder.translationLayout.also { layout ->
            layout.setOnKeyPressListener(onKeyEventListener = this@UI)
        }
    }

    override fun renderSourceLanguageSpinner(sourceLanguage: String) = launch {
        translationBinder.sourceLanguageSpinner.also { spinner ->
            val languages = languages
            if (spinner.adapter == null) {
                val adapter = ArrayAdapter(
                    this@UI,
                    R.layout.language_item_layout,
                    languages.values.toTypedArray()
                )

                spinner.adapter = adapter
                spinner.onItemSelectedListener = SpinnerItemSelectedListener { index ->
                    val selectedLanguage = languages.keys.elementAt(index)
                    preference.sourceLanguage = selectedLanguage

                    viewModel.setSourceLanguage(language = selectedLanguage)
                    viewModel.translate(sourceLanguage = selectedLanguage)
                }

                spinner.setSelection(languages.keys.indexOf(sourceLanguage))
            }

            val languageIndex = languages.keys.indexOf(sourceLanguage)
            if (languageIndex != spinner.selectedItemPosition) {
                spinner.setSelection(languageIndex)
            }
        }
    }

    override fun renderSwapLanguageIcon(
        sourceLanguage: String,
        targetLanguage: String
    ) = launch {
        translationBinder.swapLanguageIcon.also { icon ->
            icon.setOnClickListener {
                if (sourceLanguage != LANGUAGE_AUTO) {
                    preference.sourceLanguage = targetLanguage
                    preference.targetLanguage = sourceLanguage

                    viewModel.swapLanguage(
                        sourceLanguage = sourceLanguage,
                        targetLanguage = targetLanguage
                    )
                    tracker.trackSwapLanguage(sourceLanguage, targetLanguage)
                }
            }
        }
    }

    override fun renderTargetLanguageSpinner(targetLanguage: String) = launch {
        translationBinder.targetLanguageSpinner.also { spinner ->
            val languages = languages.filter { it.key != LANGUAGE_AUTO }
            if (spinner.adapter == null) {
                val adapter = ArrayAdapter(
                    this@UI,
                    R.layout.language_item_layout,
                    languages.values.toTypedArray()
                )

                spinner.adapter = adapter
                spinner.onItemSelectedListener = SpinnerItemSelectedListener { index ->
                    val selectedLanguage = languages.keys.elementAt(index)
                    preference.targetLanguage = selectedLanguage

                    viewModel.setTargetLanguage(language = selectedLanguage)
                    viewModel.translate(targetLanguage = selectedLanguage)
                }

                spinner.setSelection(languages.keys.indexOf(targetLanguage))
            }

            val languageIndex = languages.keys.indexOf(targetLanguage)
            if (languageIndex != spinner.selectedItemPosition) {
                spinner.setSelection(languageIndex)
            }
        }
    }

    override fun renderQueryInput(
        sourceLanguage: String,
        targetLanguage: String,
        instantQuery: String?
    ) = launch {
        translationBinder.queryInput.also { input ->
            instantQuery?.let { query ->
                input.setText(query)
                input.setSelection(query.length)
            }

            input.setRawInputType(TYPE_CLASS_TEXT)

            input.addQueryChangedListener(
                threadScope = this,
                onQueryChanged = { query ->
                    viewModel.setQuery(query)
                },
                onQueryDone = { query ->
                    if (preference.useResponsiveTranslator) {
                        viewModel.translate(query = query)
                        tracker.trackTranslationData(sourceLanguage, targetLanguage, query)
                    }
                }
            )

            input.addQueryActionListener(
                threadScope = this,
                onQueryDone = { query ->
                    if (!preference.useResponsiveTranslator) {
                        viewModel.translate(query = query)
                        tracker.trackTranslationData(sourceLanguage, targetLanguage, query)
                    }

                    viewModel.stopEditing()
                    tracker.trackStopEditing(by = METHOD_KEYBOARD)
                }
            )

            input.setOnFocusChangeListener { _, focus ->
                if (!focus) return@setOnFocusChangeListener
                viewModel.startEditing()
            }

            input.setOnLongClickListener {
                tracker.trackShowContextMenu()
                false
            }
        }
    }

    override fun renderClearQueryIcon(query: String) = launch {
        translationBinder.clearQueryIcon.also { icon ->
            icon.visibility = VISIBLE.takeIf { query.isNotEmpty() } ?: INVISIBLE
            icon.setOnClickListener {
                viewModel.clearQuery()
                tracker.trackClearQuery()
            }
        }
    }

    override fun renderTranslationInput(
        sourceLanguage: String,
        targetLanguage: String,
        query: String,
        data: Data<Translation>
    ) = launch {
        translationBinder.translationInput.also { input ->
            input.alpha = 0.5f.takeIf { data.isLoading() || data.isError() } ?: 1f
            input.text = data.result?.translatedText.orEmpty()
            input.isEnabled = data.let {
                !it.isLoading() && !it.result?.translatedText.isNullOrBlank()
            }

            input.setOnClickListener {
                viewModel.displayResultDetail()
                tracker.trackDisplayResultDetail(
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    query = query,
                    result = data.result?.translatedText.orEmpty()
                )
            }
        }
    }

    override fun renderLoadingProgress(data: Data<Translation>) = launch {
        translationBinder.loadingProgress.also { progress ->
            progress.visibility = VISIBLE.takeIf {
                data.isLoading() || data.isError()
            } ?: INVISIBLE
        }
    }

    override fun renderSwipeLayout() = launch {
        translationBinder.swipeLayout.also { layout ->
            layout.setOnTouchListener(
                LayoutMovementListener(
                    windowManager = layoutManager.windowManager,
                    layoutParams = translationLayoutParams,
                    onClickLayout = {},
                    onMoveLayout = { params ->
                        layoutManager.updateLayout(translationBinder.translationLayout, params)
                    }
                )
            )
        }
    }

    override fun processPacket() {
        if (::packet.isInitialized.not()) return
        packet.instantQuery?.let(::instantTranslate)
    }

    override fun showToggleLayout() {
        layoutManager.addLayout(
            toggleBinder.toggleLayout,
            toggleLayoutParams.also { it.gravity = START }
        )
    }

    override fun addTranslationLayout() {
        translationBinder.translationLayout.also { layout ->
            layout.visibility = GONE

            layoutManager.addLayout(
                layout,
                translationLayoutParams.also { it.gravity = START or TOP }
            )
        }
    }

    override fun showTranslationLayout(editMode: Boolean) {
        translationBinder.translationLayout.visibility = VISIBLE

        if (!editMode) return
        launch(coroutineContext) {
            delay(100)
            translationBinder.queryInput.requestFocus()
        }
    }

    private fun updateTranslationLayout(flag: Int) {
        layoutManager.updateLayout(
            translationBinder.translationLayout,
            translationLayoutParams.also { it.flags = flag }
        )
    }

    override fun hideTranslationLayout() {
        translationBinder.translationLayout.visibility = GONE
    }

    override fun showBackground() {
        Intent(this, UIBackground::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.run(::startActivity)
    }

    override fun onBackgroundShown() = Unit

    override fun dismissBackground() {
        Intent(SUBJECT_DISMISS_BACKGROUND).run(::sendBroadcast)
    }

    override fun onBackgroundDismissed() {
        viewModel.stopEditing()
    }

    override fun showSoftKeyboard() {
        updateTranslationLayout(FLAG_NOT_TOUCH_MODAL)
        translationBinder.queryInput.also { input ->
            launch(coroutineContext) {
                delay(150)
                input.showSoftKeyboard()
            }
        }
    }

    override fun hideSoftKeyboard() {
        translationBinder.queryInput.also { input ->
            input.clearFocus()
            input.hideSoftKeyboard()
        }
        updateTranslationLayout(FLAG_NOT_FOCUSABLE)
    }

    override fun onHideSoftKeyboard() {
        viewModel.stopEditing()
        tracker.trackStopEditing(by = METHOD_BACK_PRESSED)
    }

    override fun instantTranslate(instantQuery: String) {
        viewModel.setToggleActivation(
            active = true,
            editMode = false,
            autoClearHistory = preference.autoClearHistory
        )

        viewModel.setSourceLanguage(language = LANGUAGE_AUTO)
        viewModel.setInstantQuery(instantQuery = instantQuery)

        // We only do instant translate if responsive translator
        // preference is not set.
        if (!preference.useResponsiveTranslator) {
            viewModel.translate(query = instantQuery)
        }
        tracker.trackToggleActivation(active = true)
    }

    override fun openGoogleTranslate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ) {
        try {
            val uri = retrieveDeeplink(
                baseUrl = preference.googleTranslateUrl,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                query = query
            ).toUri()

            Intent(ACTION_VIEW, uri).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }.also(::startActivity)
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }

    override fun stopService() {
        viewModel.stopEditing()
        tracker.trackToggleService(active = false)

        stopSelf()
    }

    override fun onDestroy() {
        dispatcher.clear()
        notification.dismiss(appInfo.id)

        with(layoutManager) {
            removeLayout(toggleBinder.toggleLayout)
            removeLayout(translationBinder.translationLayout)
        }
        super.onDestroy()
    }

    companion object {
        private const val MAX_FLOATING_DIFF_DISTANCE: Int = 16

        private const val METHOD_KEYBOARD: String = "keyboard"
        private const val METHOD_BACK_PRESSED: String = "back_pressed"

        val toggleLayoutParams: LayoutParams =
            if (minOreoSdk) {
                LayoutParams(
                    WRAP_CONTENT,
                    WRAP_CONTENT,
                    TYPE_APPLICATION_OVERLAY,
                    FLAG_NOT_FOCUSABLE,
                    TRANSLUCENT
                )
            } else {
                LayoutParams(
                    WRAP_CONTENT,
                    WRAP_CONTENT,
                    TYPE_SYSTEM_ALERT,
                    FLAG_NOT_FOCUSABLE,
                    TRANSLUCENT
                )
            }

        val translationLayoutParams: LayoutParams =
            if (minOreoSdk) {
                LayoutParams(
                    MATCH_PARENT,
                    WRAP_CONTENT,
                    TYPE_APPLICATION_OVERLAY,
                    FLAG_NOT_FOCUSABLE,
                    TRANSLUCENT
                )
            } else {
                LayoutParams(
                    MATCH_PARENT,
                    WRAP_CONTENT,
                    TYPE_SYSTEM_ALERT,
                    FLAG_NOT_FOCUSABLE,
                    TRANSLUCENT
                )
            }
    }
}
