package com.bael.kirin.feature.translation.screen.settings

import com.bael.kirin.lib.arch.base.BaseState

/**
 * Created by ErickSumargo on 01/06/20.
 */

data class State(
    val version: String = "",
    val settings: Map<Pair<String, Boolean>, Pair<String, String>> = mapOf()
) : BaseState()
