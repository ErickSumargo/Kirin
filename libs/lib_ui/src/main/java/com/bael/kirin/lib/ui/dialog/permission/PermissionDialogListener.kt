package com.bael.kirin.lib.ui.dialog.permission

import androidx.fragment.app.DialogFragment

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface PermissionDialogListener {

    fun onDialogNegativeClick(dialog: DialogFragment)

    fun onDialogPositiveClick(dialog: DialogFragment)
}
