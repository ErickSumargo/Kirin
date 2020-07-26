package com.bael.kirin.feature.translation.ext

import android.text.TextWatcher
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.doAfterTextChanged
import com.bael.kirin.lib.threading.ext.safeOffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by ErickSumargo on 15/06/20.
 */

private val memCacheTextWatchers: HashMap<EditText, TextWatcher> = hashMapOf()

@FlowPreview
@ExperimentalCoroutinesApi
fun EditText.addQueryChangedListener(
    scope: CoroutineScope,
    onQueryChanged: suspend (String) -> Unit,
    onQueryFixed: suspend (String) -> Unit,
    onQueryDone: suspend (String) -> Unit
) {
    callbackFlow<String> {
        removeTextChangedListener(memCacheTextWatchers[this@addQueryChangedListener])

        val textWatcher = doAfterTextChanged { editor ->
            safeOffer(editor.toString())
        }.also {
            memCacheTextWatchers[this@addQueryChangedListener] = it
        }

        awaitClose {
            memCacheTextWatchers.clear()
            removeTextChangedListener(textWatcher)
        }
    }
        .onEach(onQueryChanged)
        .debounce(500)
        .filter { it.isNotEmpty() }
        .onEach(onQueryFixed)
        .launchIn(scope)

    callbackFlow<String> {
        OnEditorActionListener { editor, action, _ ->
            when (action) {
                IME_ACTION_DONE -> {
                    safeOffer(editor.text.toString())
                    false
                }
                else -> true
            }
        }.also(::setOnEditorActionListener)

        awaitClose { setOnEditorActionListener(null) }
    }
        .filter { it.isNotEmpty() }
        .onEach(onQueryDone)
        .launchIn(scope)
}
