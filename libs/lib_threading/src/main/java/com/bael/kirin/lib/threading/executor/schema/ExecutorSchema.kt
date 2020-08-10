package com.bael.kirin.lib.threading.executor.schema

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class ExecutorSchema {

    object Default : ExecutorSchema()

    object Queue : ExecutorSchema()

    object Conflated : ExecutorSchema()
}
