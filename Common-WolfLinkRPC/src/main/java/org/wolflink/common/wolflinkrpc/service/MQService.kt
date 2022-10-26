package org.wolflink.common.wolflinkrpc.service

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.ConsoleSender
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.listener.OnDatapackReceive
import java.util.List

object MQService {

    lateinit var connectionFactory: ConnectionFactory
    lateinit var connection: Connection
    lateinit var channel: Channel
    lateinit var consumer : OnDatapackReceive
    lateinit var queueName : String

    //初始化消息队列服务
    fun init(configuration : IConfiguration)
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
        MQService.channel.exchangeDeclare(ExchangeType.SINGLE_EXCHANGE.name.lowercase(), BuiltinExchangeType.DIRECT)
        // 声明组交换机
        MQService.channel.exchangeDeclare(ExchangeType.GROUP_EXCHANGE.name.lowercase(), BuiltinExchangeType.DIRECT)
        // 声明广播交换机
        MQService.channel.exchangeDeclare(ExchangeType.ALL_EXCHANGE.name.lowercase(), BuiltinExchangeType.FANOUT)

        // 声明客户端归属队列(唯一)，以其对应的Consumer
        MQService.channel.queueDeclare(configuration.getQueueName(),false,false,true,null)
        MQService.channel.basicConsume(configuration.getQueueName(),true,MQService.consumer)

        // 定向交换机绑定
        MQService.channel.queueBind(configuration.getQueueName(),ExchangeType.SINGLE_EXCHANGE.name.lowercase(),configuration.getQueueName())
        // 组交换机绑定
        MQService.channel.queueBind(configuration.getQueueName(),ExchangeType.GROUP_EXCHANGE.name.lowercase(),"broadcast."+configuration.getClientType().name.lowercase())
        // 广播交换机绑定
        MQService.channel.queueBind(configuration.getQueueName(),ExchangeType.ALL_EXCHANGE.name.lowercase(),"broadcast.all")

        RPCCore.logger.info("Message queue service has been initialized")
    }

    /**
     * 异步发送数据包
     *
     * @datapack 数据包本体
     * @callback 是否需要回调
     * @callbackFunction 回调函数
     * @failedSec 回调TTL
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun sendDataPack(datapack : RPCDataPack, callback : Boolean, callbackFunction: CallbackFunction? = null, failedSec : Int? = null)
    {
        if(callback && callbackFunction != null) consumer.addCallback(datapack, callbackFunction,failedSec)

        for (routingData in datapack.routingDataList)
        {
            for (routingKey in routingData.routingKeyList)
            {
                GlobalScope.launch {
                    channel.basicPublish(routingData.exchangeType.name.lowercase(),routingKey,null,
                        RPCDataPack.toJson(datapack).toByteArray())
                }
            }
        }
        RPCCore.logger.info("a datapack has been send.")
    }
    //发送一个不需要反馈的数据包
    fun sendDataPack(datapack: RPCDataPack)
    {
        sendDataPack(datapack,false)
    }
    //发送一个指令回馈数据包
    fun sendCommandFeedBack(originPack : RPCDataPack,queueName : String,feedbackBody : ICommandResultBody)
    {
        val feedbackPack = RPCDataPack.Builder()
            .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(originPack.senderName)))
            .setType(DataPackType.COMMAND_RESULT)
            .setSenderName(queueName)
            .setUUID(originPack.uuid)
            .setDatapackBody(feedbackBody)
            .build()

        sendDataPack(feedbackPack)
    }
    fun sendOnlineMessage() {
        val textMessageBody: ITextMessageBody = object : ITextMessageBody {
            override fun getSender(): ISender = ConsoleSender(RPCCore.configuration.getQueueName(), RPCCore.configuration.getClientType())

            override fun getMsg(): String {
                return "用户端 ${RPCCore.configuration.getQueueName()} 已上线"
            }
        }
        val rpcDataPack = RPCDataPack.Builder()
            .setDatapackBody(textMessageBody)
            .setSenderName(RPCCore.configuration.getQueueName())
            .setType(DataPackType.TEXT_MESSAGE)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .build()
        MQService.sendDataPack(rpcDataPack)
    }
    fun sendOfflineMessage(){
        val textMessageBody: ITextMessageBody = object : ITextMessageBody {
            override fun getSender(): ISender = ConsoleSender(RPCCore.configuration.getQueueName(), RPCCore.configuration.getClientType())

            override fun getMsg(): String {
                return "用户端 ${RPCCore.configuration.getQueueName()} 已离线"
            }
        }
        val rpcDataPack = RPCDataPack.Builder()
            .setDatapackBody(textMessageBody)
            .setSenderName(RPCCore.configuration.getQueueName())
            .setType(DataPackType.TEXT_MESSAGE)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .build()
        MQService.sendDataPack(rpcDataPack)
    }
}