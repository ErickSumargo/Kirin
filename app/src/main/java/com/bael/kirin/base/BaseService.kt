package com.bael.kirin.base

import android.app.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 01/06/20.
 */

abstract class BaseService(
    override val coroutineContext: CoroutineContext = Main
) : Service(),
    CoroutineScope
