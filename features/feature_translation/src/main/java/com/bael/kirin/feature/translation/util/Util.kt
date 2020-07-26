package com.bael.kirin.feature.translation.util

import android.content.Context
import android.provider.Settings.canDrawOverlays
import com.bael.kirin.lib.resource.util.Util.minMarshmallowSdk
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by ErickSumargo on 01/06/20.
 */

object Util {
    internal val Context.canDrawOverlays: Boolean
        get() = minMarshmallowSdk && canDrawOverlays(this)

    fun calculateDistance(coordsStart: Pair<Int, Int>, coordsEnd: Pair<Int, Int>): Double {
        val (xStart, yStart) = coordsStart
        val (xEnd, yEnd) = coordsEnd

        val dx = (xEnd - xStart).toDouble()
        val dy = (yEnd - yStart).toDouble()
        return sqrt((dx.pow(2) + dy.pow(2)))
    }

    fun retrieveDeeplink(
        baseUrl: String,
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): String {
        return baseUrl
            .replace("%0%", sourceLanguage)
            .replace("%1%", targetLanguage)
            .replace("%2%", query)
    }
}
