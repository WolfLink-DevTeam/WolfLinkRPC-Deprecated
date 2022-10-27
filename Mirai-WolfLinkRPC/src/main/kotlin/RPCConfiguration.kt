package org.wolflink.mirai.wolflinkrpc

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger

object RPCConfiguration : IConfiguration {

    override fun getAnalyseFunctionPackage(): String = "org.wolflink.mirai.wolflinkrpc.analyse"

    override fun getClientType(): ClientType = ClientType.MIRAI

    override fun getHost(): String = PersistenceConfig.host

    override fun getLogger(): ILogger = RPCLogger

    override fun getMainClass(): Class<*> = App::class.java

    override fun getPassword(): String = PersistenceConfig.password
    override fun getPermissionGroupMap() = PersistenceConfig.permissionMap

    override fun getPort(): Int = PersistenceConfig.port

    override fun getQueueName(): String = "${getClientType().name.lowercase()}_${PersistenceConfig.clientTag}"

    override fun getUsername(): String = PersistenceConfig.username

    override fun getCommandFunctionPackage() = "org.wolflink.mirai.wolflinkrpc.command"

}