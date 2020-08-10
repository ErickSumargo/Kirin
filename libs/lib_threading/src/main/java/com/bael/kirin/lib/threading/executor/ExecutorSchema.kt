package com.bael.kirin.lib.threading.executor

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class ExecutorSchema {

    object Concurrent : ExecutorSchema()

    object Queue : ExecutorSchema()

    object Conflated : ExecutorSchema()
}
