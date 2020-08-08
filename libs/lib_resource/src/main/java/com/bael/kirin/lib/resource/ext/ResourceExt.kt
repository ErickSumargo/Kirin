package com.bael.kirin.lib.resource.ext

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Html.fromHtml
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.bael.kirin.lib.resource.util.Util.minNougatSdk

/**
 * Created by ErickSumargo on 01/06/20.
 */

fun Context.textOf(
    resId: Int,
    vararg params: String
): String {
    return getString(resId, *params)
}

@SuppressLint("InlinedApi")
fun Context.textHtmlOf(
    resId: Int,
    vararg params: String
): CharSequence {
    val text = getString(resId, *params)
    return if (minNougatSdk) {
        fromHtml(text, FROM_HTML_MODE_LEGACY)
    } else {
        fromHtml(text)
    }
}

fun Context.showMessage(message: String) {
    if (message.isBlank()) return
    Toast.makeText(this, message, LENGTH_SHORT).show()
}
