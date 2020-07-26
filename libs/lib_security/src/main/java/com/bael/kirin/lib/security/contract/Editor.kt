package com.bael.kirin.lib.security.contract

import java.io.FileInputStream

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Editor {

    fun readFile(fileName: String): FileInputStream?

    /**
     * @param callback with flag as indicator if write process is success.
     */
    fun writeFile(
        fileName: String,
        data: String?,
        callback: (Boolean) -> Unit
    )
}
