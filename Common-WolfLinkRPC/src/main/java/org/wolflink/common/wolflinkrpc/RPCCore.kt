package org.wolflink.common.wolflinkrpc

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger
import org.wolflink.common.wolflinkrpc.service.CommandService
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.common.wolflinkrpc.service.PermissionService
import org.wolflink.common.wolflinkrpc.service.RPCService

object RPCCore {

    lateinit var logger : ILogger

    @Deprecated("尽量不使用，将配置项全部注入到具体业务类中，不进行配置集成")
    lateinit var configuration: IConfiguration

    /**
     * 由客户端启动时调用
     */
    fun initSystem(configuration: IConfiguration)
    {
        this.configuration = configuration

        initLogger(configuration)

        PermissionService.init(configuration)

        if(configuration.getClientType() != ClientType.MIRAI)
        {
            CommandService.init(configuration)
            RPCService.init(configuration)
        }else
        {
            configuration.getLogger().info("Mirai 插件不支持使用反射，已禁用相关功能。")
        }

        MQService.init(configuration)

        MQService.sendOnlineMessage()
    }
    /**
     * 回收资源，结束进程
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun closeSystem()
    {
        MQService.sendOfflineMessage()
        try
        {
            GlobalScope.launch {
                delay(1)
                MQService.channel.close()
                MQService.connection.close()
            }
        } catch (ignore : java.lang.Exception){}

        logger.info("System has been closed")
    }


    //初始化日志系统
    private fun initLogger(configuration: IConfiguration)
    {
        logger = configuration.getLogger()
        logger.info("Logger has been initialized")
    }



}