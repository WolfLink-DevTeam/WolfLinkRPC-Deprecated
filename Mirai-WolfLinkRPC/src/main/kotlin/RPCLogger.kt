package org.wolflink.mirai.wolflinkrpc

import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger

object RPCLogger : ILogger {

    val logger = App.logger

    override fun debug(str: String) {
        logger.debug(str)
    }

    override fun error(str: String) {
        logger.error(str)
    }

    override fun info(str: String) {
        logger.info(str)
    }

    override fun warn(str: String) {
        logger.warning(str)
    }
}