package com.bael.kirin.lib.data.ext

import com.bael.kirin.lib.data.model.Data

/**
 * Created by ErickSumargo on 01/06/20.
 */

fun <T> Data<T>?.orDefault(): Data<T> = this ?: Data()
