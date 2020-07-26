package com.bael.kirin.lib.arch.wrapper

/**
 * Created by ErickSumargo on 01/06/20.
 */

class LazyWrapper<T>(private val _instance: T) {
    val instance: Lazy<T> = object : Lazy<T> {
        override val value: T = _instance
        override fun isInitialized(): Boolean = true
    }

    operator fun invoke(): T = instance.value
}
