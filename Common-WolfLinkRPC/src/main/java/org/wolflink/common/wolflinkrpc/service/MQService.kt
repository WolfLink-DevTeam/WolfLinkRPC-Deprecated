package org.wolflink.common.wolflinkrpc.service

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.listener.OnDatapackReceive
import java.util.List

object MQService {

    lateinit var connectionFactory: ConnectionFactory
    lateinit var connection: Connection
    lateinit var channel: Channel
    lateinit var consumer : OnDatapackReceive
    lateinit var queueName : String

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
}