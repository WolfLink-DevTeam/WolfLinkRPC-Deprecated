package org.wolflink.common.wolflinkrpc.api.interfaces

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IRemoteHandler
import org.wolflink.common.wolflinkrpc.utils.ReflectionUtil

interface IConfiguration {
    fun getHost() : String
    fun getPort() : Int
    fun getUsername() : String
    fun getPassword() : String
    fun getQueueName() : String
    fun getLogger() : ILogger
    fun getClientType() : ClientType
    //默认通过反射获取
    fun getRemoteCallHandlerList() : MutableList<IRemoteHandler>
    {
        val list = mutableListOf<IRemoteHandler>()
        val classes = ReflectionUtil.getClassesByAnnotation(getRemoteCallHandlerPackage(),RemoteCallHandler::class.java)
        for (clazz in classes) list.add(clazz.getConstructor().newInstance() as IRemoteHandler)
        RPCCore.logger.info("AnalyseFunction has been initialized , ${list.size} are now available.")
        return list
    }
    fun getRemoteCallHandlerPackage() : String
    fun getMainClass() : Class<*>
    fun getLocalCallHandlerPackage() : String
    // key-用户唯一UUID value-权限等级
    fun getPermissionGroupMap() : MutableMap<String,PermissionLevel>
}