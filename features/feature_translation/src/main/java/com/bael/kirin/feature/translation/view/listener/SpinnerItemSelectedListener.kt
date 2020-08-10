package com.bael.kirin.feature.translation.view.listener

import android.view.View
import android.widget.AdapterView

/**
 * Created by ErickSumargo on 01/06/20.
 */

class SpinnerItemSelectedListener(
    private val onItemSelected: (Int) -> Unit
) : AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) = onItemSelected(pos)
}
