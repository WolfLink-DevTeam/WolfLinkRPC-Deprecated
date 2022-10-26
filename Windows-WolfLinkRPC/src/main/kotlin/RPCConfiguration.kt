package org.wolflink.windows.wolflinkrpc

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger

object RPCConfiguration : IConfiguration {
    override fun getHost(): String = "43.248.79.66"

    override fun getPort(): Int = 55559

    override fun getUsername(): String = "testuser"

    override fun getPassword(): String = "mikkoayaka"

    override fun getQueueName(): String = "windows_1"

    override fun getLogger(): ILogger = RPCLogger

    override fun getClientType(): ClientType = ClientType.WINDOWS

    override fun getAnalyseFunctionPackage(): String = "org.wolflink.windows.wolflinkrpc.analyse"

    override fun getMainClass(): Class<*> = App::class.java

    override fun getCommandFunctionPackage(): String = "org.wolflink.windows.wolflinkrpc.command"
    override fun getPermissionGroupMap(): MutableMap<String, PermissionLevel> = mutableMapOf()
}