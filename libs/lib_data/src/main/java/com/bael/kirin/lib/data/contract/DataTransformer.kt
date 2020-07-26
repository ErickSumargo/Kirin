package com.bael.kirin.lib.data.contract

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface DataTransformer<R, out D> {

    fun transform(response: R): D
}
