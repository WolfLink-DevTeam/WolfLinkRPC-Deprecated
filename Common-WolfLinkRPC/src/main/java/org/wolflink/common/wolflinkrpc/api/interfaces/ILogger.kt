package org.wolflink.common.wolflinkrpc.api.interfaces

interface ILogger {
    fun debug(str : String)
    fun info(str : String)
    fun warn(str : String)
    fun error(str : String)
}