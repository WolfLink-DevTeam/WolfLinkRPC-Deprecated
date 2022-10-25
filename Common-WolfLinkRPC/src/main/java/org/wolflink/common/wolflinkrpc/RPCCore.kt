package org.wolflink.common.wolflinkrpc

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody
import org.wolflink.common.wolflinkrpc.entity.CommandData
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.ConsoleSender
import org.wolflink.common.wolflinkrpc.listener.OnDatapackReceive
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.common.wolflinkrpc.service.RPCService
import org.wolflink.common.wolflinkrpc.utils.ReflectionUtil

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
        if(configuration.getClientType() != ClientType.MIRAI)
        {
            initCommands(configuration)
            initRPCService(configuration)
        }else
        {
            configuration.getLogger().info("Mirai 插件不支持使用反射，已禁用相关功能。")
        }

        initMQService(configuration)

        sendOnlineMessage()
    }

    /**
     * 回收资源，结束进程
     */
    fun closeSystem()
    {
        sendOfflineMessage()
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

    //初始化指令部分，用反射实现
    private fun initCommands(configuration: IConfiguration)
    {
        var count = 0
        val classes = ReflectionUtil.getClassesByAnnotation(configuration.getCommandFunctionPackage(),CommandFunction::class.java)
        for (clazz in classes)
        {
            val iCommandFunction = clazz.getConstructor().newInstance() as ICommandFunction
            CommandData.bindCommand(iCommandFunction)
            count++
        }
        logger.info("CommandData has been initialized , $count commands are now available.")
    }
    //初始化日志系统
    private fun initLogger(configuration: IConfiguration)
    {
        logger = configuration.getLogger()
        logger.info("Logger has been initialized")
    }
    //初始化消息队列服务
    private fun initMQService(configuration : IConfiguration)
    {
        MQService.connectionFactory = ConnectionFactory()
        MQService.connectionFactory.host = configuration.getHost()
        MQService.connectionFactory.port = configuration.getPort()
        MQService.connectionFactory.username = configuration.getUsername()
        MQService.connectionFactory.password = configuration.getPassword()
        MQService.queueName = configuration.getQueueName()

        //不关闭资源，需要保持连接
        MQService.connection = MQService.connectionFactory.newConnection()
        MQService.channel = MQService.connection.createChannel()
        MQService.consumer = OnDatapackReceive(MQService.channel)

        // 声明定向交换机
        MQService.channel.exchangeDeclare(ExchangeType.SINGLE_EXCHANGE.name.lowercase(),BuiltinExchangeType.DIRECT)
        // 声明组交换机
        MQService.channel.exchangeDeclare(ExchangeType.GROUP_EXCHANGE.name.lowercase(),BuiltinExchangeType.DIRECT)
        // 声明广播交换机
        MQService.channel.exchangeDeclare(ExchangeType.ALL_EXCHANGE.name.lowercase(),BuiltinExchangeType.FANOUT)

        // 声明客户端归属队列(唯一)，以其对应的Consumer
        MQService.channel.queueDeclare(configuration.getQueueName(),false,false,true,null)
        MQService.channel.basicConsume(configuration.getQueueName(),true,MQService.consumer)

        // 定向交换机绑定
        MQService.channel.queueBind(configuration.getQueueName(),ExchangeType.SINGLE_EXCHANGE.name.lowercase(),configuration.getQueueName())
        // 组交换机绑定
        MQService.channel.queueBind(configuration.getQueueName(),ExchangeType.GROUP_EXCHANGE.name.lowercase(),"broadcast."+configuration.getClientType().name.lowercase())
        // 广播交换机绑定
        MQService.channel.queueBind(configuration.getQueueName(),ExchangeType.ALL_EXCHANGE.name.lowercase(),"broadcast.all")

        logger.info("Message queue service has been initialized")
    }

    //初始化数据包处理服务
    private fun initRPCService(configuration: IConfiguration)
    {
        RPCService.analyseFunctionList = configuration.getAnalyseFunctionList()
    }
    private fun sendOnlineMessage() {
        val textMessageBody: ITextMessageBody = object : ITextMessageBody {
            override fun getSender(): ISender = ConsoleSender(configuration.getQueueName(), configuration.getClientType())

            override fun getMsg(): String {
                return "用户端 ${configuration.getQueueName()} 已上线"
            }
        }
        val rpcDataPack = RPCDataPack.Builder()
            .setDatapackBody(textMessageBody)
            .setSenderName(configuration.getQueueName())
            .setType(DataPackType.TEXT_MESSAGE)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .build()
        MQService.sendDataPack(rpcDataPack)
    }
    private fun sendOfflineMessage(){
        val textMessageBody: ITextMessageBody = object : ITextMessageBody {
            override fun getSender(): ISender = ConsoleSender(configuration.getQueueName(), configuration.getClientType())

            override fun getMsg(): String {
                return "用户端 ${configuration.getQueueName()} 已离线"
            }
        }
        val rpcDataPack = RPCDataPack.Builder()
            .setDatapackBody(textMessageBody)
            .setSenderName(configuration.getQueueName())
            .setType(DataPackType.TEXT_MESSAGE)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .build()
        MQService.sendDataPack(rpcDataPack)
    }
}