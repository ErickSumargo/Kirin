package com.bael.kirin.lib.ui.dialog.permission

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bael.kirin.lib.logger.contract.Logger
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class PermissionDialog(
    private val title: String,
    private val message: CharSequence,
    private val actionNegativeText: String,
    private val actionPositiveText: String
) : DialogFragment() {
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
}
