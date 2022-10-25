package org.wolflink.windows.wolflinkrpc

import org.wolflink.common.wolflinkrpc.BaseConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger
import java.text.SimpleDateFormat
import java.util.*

object RPCLogger : ILogger {

    fun getTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        return sdf.format(Date())
    }

    override fun debug(str: String) {
        println("[Debug][${getTime()}] $str")
    }

    override fun info(str: String) {
        println("[Info ][${getTime()}] $str")
    }

    override fun warn(str: String) {
        println("[Warn ][${getTime()}] $str")
    }

    override fun error(str: String) {
        println("[Error][${getTime()}] $str")
    }
}