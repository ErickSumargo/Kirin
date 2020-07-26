package com.bael.kirin.feature.translation.screen.settings

/**
 * Created by ErickSumargo on 01/06/20.
 */

data class State(
    val version: String = "",
    val settings: Map<Pair<String, Boolean>, Pair<String, String>> = mapOf()
)
