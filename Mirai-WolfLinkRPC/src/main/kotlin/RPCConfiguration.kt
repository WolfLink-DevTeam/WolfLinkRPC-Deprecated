package org.wolflink.mirai.wolflinkrpc

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger

object RPCConfiguration : IConfiguration {

    override fun getAnalyseFunctionPackage(): String = "org.wolflink.mirai.wolflinkrpc.analyse"

    override fun getClientType(): ClientType = ClientType.MIRAI

    override fun getHost(): String = "43.248.79.66"

    override fun getLogger(): ILogger = RPCLogger

    override fun getMainClass(): Class<*> = App::class.java

    override fun getPassword(): String = "mikkoayaka"
    override fun getPermissionGroupMap(): MutableMap<String, PermissionLevel> = mutableMapOf("3401286177" to PermissionLevel.ONLY_MANAGER)

    override fun getPort(): Int = 55559

    override fun getQueueName(): String = "mirai_1"

    override fun getUsername(): String = "testuser"

    override fun getCommandFunctionPackage() = "org.wolflink.mirai.wolflinkrpc.command"

}