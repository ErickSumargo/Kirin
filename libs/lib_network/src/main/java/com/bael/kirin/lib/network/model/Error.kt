package com.bael.kirin.lib.network.model

/**
 * Created by ErickSumargo on 15/06/20.
 */

open class Error(
    private val code: Int = -1,
    override val message: String = ""
) : Exception()
