package com.bael.kirin.lib.ui.dialog.progress

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.ui.databinding.ProgressDialogLayoutBinding
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class ProgressDialog : DialogFragment() {
    @Inject
    lateinit var logger: Logger

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return try {
            val message = arguments?.getCharSequence(ARG_MESSAGE)

            val layout = ProgressDialogLayoutBinding.inflate(layoutInflater)
            layout.messageLabel.also { label ->
                label.text = message
            }

            AlertDialog.Builder(activity)
                .setView(layout.root)
                .create()
        } catch (cause: Exception) {
            logger.log(cause)
            Dialog(context!!)
        }
    }

    companion object {
        private const val ARG_MESSAGE: String = "message"

        fun create(
            message: CharSequence
        ): ProgressDialog {
            val data = bundleOf(
                ARG_MESSAGE to message
            )

            val dialog = ProgressDialog().apply {
                arguments = data
                isCancelable = false
            }
            return dialog
        }
    }
}
