package com.bael.kirin.feature.translation.screen.settings

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Renderer {

    fun renderVersion(version: String)

    fun renderSettings(settings: Map<Pair<String, Boolean>, Pair<String, String>>)
}
