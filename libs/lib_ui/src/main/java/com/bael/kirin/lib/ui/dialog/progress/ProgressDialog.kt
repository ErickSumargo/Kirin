package com.bael.kirin.lib.ui.dialog.progress

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.ui.databinding.ProgressDialogLayoutBinding
import com.bael.kirin.lib.ui.dialog.permission.PermissionDialog
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
        ): PermissionDialog {
            val data = Bundle().apply {
                putCharSequence(ARG_MESSAGE, message)
            }

            val dialog = PermissionDialog().apply {
                arguments = data
                isCancelable = false
            }
            return dialog
        }
    }
}
