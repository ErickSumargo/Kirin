package com.bael.kirin

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bael.kirin.State.Editing

/**
 * Created by ErickSumargo on 01/06/20.
 */

fun Context.showSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(SHOW_FORCED, 0)
}

fun Context.hideSoftKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

inline fun <reified S> BasePresenter<*>.render(
    crossinline callback: (S) -> Unit
) {
    state.observeForever { newState ->
        when (newState) {
            is S -> callback(newState as S)
            else -> Log.d("lala", "There is no such state")
        }
    }
}

class Presenter : BasePresenter<State>() {

    fun setSourceLanguageText(text: String) {
        render(Editing(text))
    }

    fun clearSourceLanguageText() {
        render(Editing(text = "", reset = true))
    }

    fun close() {
        render(State.Close)
    }
}

abstract class BasePresenter<S> {
    private val mutableState = MutableLiveData<S>()
    val state: LiveData<S> get() = mutableState

    fun render(newState: S) {
        mutableState.value = newState
    }
}

sealed class State {

    data class Editing(
        val text: String,
        val reset: Boolean = false
    ) : State()

    object Close : State()
}
