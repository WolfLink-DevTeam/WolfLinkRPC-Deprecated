package org.wolflink.common.wolflinkrpc

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger
import org.wolflink.common.wolflinkrpc.listener.OnDatapackReceive
import org.wolflink.common.wolflinkrpc.service.MQService
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
        initMQService(configuration)
        initRPCService(configuration)
    }

    /**
     * 回收资源，结束进程
     */
    fun closeSystem()
    {
        MQService.channel.close()
        MQService.connection.close()
        logger.info("System has been closed")
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
}