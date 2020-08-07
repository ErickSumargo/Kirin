package com.bael.kirin.lib.ui.dialog.permission

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bael.kirin.lib.logger.contract.Logger
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class PermissionDialog : DialogFragment() {
    @Inject
    lateinit var logger: Logger

    private lateinit var listener: PermissionDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as PermissionDialogListener
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return try {
            val title = arguments?.getCharSequence(ARG_TITLE)
            val message = arguments?.getCharSequence(ARG_MESSAGE)
            val actionNegativeText = arguments?.getCharSequence(ARG_ACTION_NEGATIVE_TEXT)
            val actionPositiveText = arguments?.getCharSequence(ARG_ACTION_POSITIVE_TEXT)

            AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(actionNegativeText) { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
                .setPositiveButton(actionPositiveText) { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .create()
        } catch (cause: Exception) {
            logger.log(cause)
            Dialog(context!!)
        }
    }

    companion object {
        private const val ARG_TITLE: String = "title"
        private const val ARG_MESSAGE: String = "message"
        private const val ARG_ACTION_NEGATIVE_TEXT: String = "action_negative_text"
        private const val ARG_ACTION_POSITIVE_TEXT: String = "action_positive_text"

        fun create(
            title: String,
            message: CharSequence,
            actionNegativeText: String,
            actionPositiveText: String
        ): PermissionDialog {
            val data = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message,
                ARG_ACTION_NEGATIVE_TEXT to actionNegativeText,
                ARG_ACTION_POSITIVE_TEXT to actionPositiveText
            )

            val dialog = PermissionDialog().apply {
                arguments = data
                isCancelable = false
            }
            return dialog
        }
    }
}
