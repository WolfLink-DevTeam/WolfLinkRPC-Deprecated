package org.wolflink.windows.wolflinkrpc

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger

object RPCConfiguration : IConfiguration {
    override fun getHost(): String = PersistenceCfg.host
    override fun getPort(): Int = PersistenceCfg.port
    override fun getUsername(): String = PersistenceCfg.username
    override fun getPassword(): String = PersistenceCfg.password
    override fun getQueueName(): String = PersistenceCfg.queueName
    override fun getLogger(): ILogger = RPCLogger
    override fun getClientType(): ClientType = ClientType.WINDOWS
    override fun getAnalyseFunctionPackage(): String = "org.wolflink.windows.wolflinkrpc.analyse"
    override fun getMainClass(): Class<*> = App::class.java
    override fun getCommandFunctionPackage(): String = "org.wolflink.windows.wolflinkrpc.command"
    override fun getPermissionGroupMap(): MutableMap<String, PermissionLevel> = mutableMapOf()
}