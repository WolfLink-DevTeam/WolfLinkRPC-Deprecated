package org.wolflink.common.wolflinkrpc.api.interfaces

import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.utils.ReflectionUtil
import java.util.UUID

interface IConfiguration {
    fun getHost() : String
    fun getPort() : Int
    fun getUsername() : String
    fun getPassword() : String
    fun getQueueName() : String
    fun getLogger() : ILogger
    fun getClientType() : ClientType
    //默认通过反射获取
    fun getAnalyseFunctionList() : MutableList<IAnalyse>
    {
        val list = mutableListOf<IAnalyse>()
        val classes = ReflectionUtil.getClassesByAnnotation(getAnalyseFunctionPackage(),AnalyseFunction::class.java)
        for (clazz in classes) list.add(clazz.getConstructor().newInstance() as IAnalyse)
        RPCCore.logger.info("AnalyseFunction has been initialized , ${list.size} are now available.")
        return list
    }
    fun getAnalyseFunctionPackage() : String
    fun getMainClass() : Class<*>
    fun getCommandFunctionPackage() : String
    // key-用户唯一UUID value-权限等级
    fun getPermissionGroupMap() : MutableMap<String,PermissionLevel>
}