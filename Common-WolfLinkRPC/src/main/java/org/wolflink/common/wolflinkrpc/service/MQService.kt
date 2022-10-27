package org.wolflink.common.wolflinkrpc.service

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.listener.OnDatapackReceive

object MQService {

    lateinit var connectionFactory: ConnectionFactory
    lateinit var connection: Connection
    lateinit var channel: Channel
    lateinit var consumer : OnDatapackReceive
    lateinit var queueName : String
    lateinit var clientType: ClientType

    //初始化消息队列服务
    fun init(configuration : IConfiguration)
    {
        connectionFactory = ConnectionFactory()
        connectionFactory.host = configuration.getHost()
        connectionFactory.port = configuration.getPort()
        connectionFactory.username = configuration.getUsername()
        connectionFactory.password = configuration.getPassword()
        queueName = configuration.getQueueName()
        clientType = configuration.getClientType()

        //不关闭资源，需要保持连接
        connection = connectionFactory.newConnection()
        channel = connection.createChannel()
        consumer = OnDatapackReceive(channel)

        // 声明定向交换机
        channel.exchangeDeclare(ExchangeType.SINGLE_EXCHANGE.name.lowercase(), BuiltinExchangeType.DIRECT)
        // 声明组交换机
        channel.exchangeDeclare(ExchangeType.GROUP_EXCHANGE.name.lowercase(), BuiltinExchangeType.DIRECT)
        // 声明广播交换机
        channel.exchangeDeclare(ExchangeType.ALL_EXCHANGE.name.lowercase(), BuiltinExchangeType.FANOUT)

        // 声明客户端归属队列(唯一)，以其对应的Consumer
        channel.queueDeclare(configuration.getQueueName(),false,false,true,null)
        channel.basicConsume(configuration.getQueueName(),true, consumer)

        // 定向交换机绑定
        channel.queueBind(configuration.getQueueName(),ExchangeType.SINGLE_EXCHANGE.name.lowercase(),configuration.getQueueName())
        // 组交换机绑定
        channel.queueBind(configuration.getQueueName(),ExchangeType.GROUP_EXCHANGE.name.lowercase(),"broadcast."+configuration.getClientType().name.lowercase())
        // 广播交换机绑定
        channel.queueBind(configuration.getQueueName(),ExchangeType.ALL_EXCHANGE.name.lowercase(),"broadcast.all")

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

        RPCCore.logger.debug("""
            [ Send Datapack ]
            SenderName = ${datapack.sender.getSenderName()}
            DatapackType = ${datapack.type.name}
            JsonObject = ${datapack.jsonObject}
        """.trimIndent())


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
    //以客户端的身份发送一个指令回馈数据包
    fun sendCommandFeedBack(originPack : RPCDataPack,feedbackBody : ICommandResultBody)
    {
        val feedbackPack = RPCDataPack.Builder()
            .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(originPack.sender.getQueueName())))
            .setType(DataPackType.COMMAND_RESULT)
            .setUUID(originPack.uuid)
            .setDatapackBody(feedbackBody)
            .build()
        sendDataPack(feedbackPack)
        RPCCore.logger.info("Send a feedback to ${originPack.sender.getQueueName()}")
    }
    fun sendOnlineMessage() {
        val textMessageBody: ITextMessageBody = object : ITextMessageBody {
            override fun getMsg(): String {
                return "用户端 $queueName 已上线"
            }
        }
        val rpcDataPack = RPCDataPack.Builder()
            .setDatapackBody(textMessageBody)
            .setType(DataPackType.TEXT_MESSAGE)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .build()
        sendDataPack(rpcDataPack)
    }
    fun sendOfflineMessage(){
        val textMessageBody: ITextMessageBody = object : ITextMessageBody {

            override fun getMsg(): String {
                return "用户端 $queueName 已离线"
            }
        }
        val rpcDataPack = RPCDataPack.Builder()
            .setDatapackBody(textMessageBody)
            .setType(DataPackType.TEXT_MESSAGE)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .build()
        sendDataPack(rpcDataPack)
    }
}