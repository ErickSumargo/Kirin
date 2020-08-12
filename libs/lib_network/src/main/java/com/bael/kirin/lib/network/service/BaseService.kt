package com.bael.kirin.lib.network.service

import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.network.base.Network
import com.bael.kirin.lib.network.model.Error
import com.bael.kirin.lib.network.model.Response
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

abstract class BaseService {
    @Inject
    lateinit var network: Network

    @Inject
    lateinit var logger: Logger

    protected inline fun <reified T> get(request: () -> T): Response<T> {
        if (!network.isConnected()) return Response(error = Error())

        return try {
            val response = request()
            Response(data = response)
        } catch (cause: Exception) {
            logger.log(cause)

            val error = Error(message = cause.message.toString())
            Response(error = error)
        }
    }
}
