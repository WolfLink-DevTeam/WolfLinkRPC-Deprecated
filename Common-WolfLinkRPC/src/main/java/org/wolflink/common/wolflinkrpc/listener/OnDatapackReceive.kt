package org.wolflink.common.wolflinkrpc.listener

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.service.RPCService
import java.util.*


class OnDatapackReceive(channel: Channel) : DefaultConsumer(channel) {

    private val callbackMap = mutableMapOf<UUID, CallbackFunction>()

    // 新增回调事件，在规定时间内没有收到回调函数则执行failedFunction
    fun addCallback(datapack: RPCDataPack, callbackFunction: CallbackFunction, failedSec : Int?)
    {
        val uuid = datapack.uuid
        if(callbackMap.containsKey(uuid))RPCCore.logger.error("CallbackMap has duplicate uuid ! It may cause errors.")
        else{
            callbackMap[uuid] = callbackFunction
            setCallbackFailed(datapack,failedSec,callbackFunction::failed)
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun setCallbackFailed(datapack : RPCDataPack, second : Int?, failedCallbackFunction: (RPCDataPack) -> Unit)
    {
        val sec = second ?: 15
        GlobalScope.launch {
            delay(1000L * sec)
            if(callbackMap.containsKey(datapack.uuid)) // 如果该UUID还留存在map里面，触发失败回调函数
            {
                callbackMap.remove(datapack.uuid)
                failedCallbackFunction.invoke(datapack)
            }
        }
    }

    override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?)
    {
        if(body == null)return
        val datapackStr = String(body)
        val datapack = RPCDataPack.fromJson(datapackStr, RPCDataPack::class.java)

        // TODO
        RPCCore.logger.debug("""
            收到数据包
            UUID - ${datapack.uuid}
            Type - ${datapack.type}
            Sender - ${datapack.senderName}
            RoutingDataList - ${datapack.routingDataList}
            JsonObject - ${datapack.jsonObject}
        """.trimIndent())

        // 消费回调队列(只在不需要反馈的情况下消费回调)
        if(!datapack.type.needFeedback && callbackMap.containsKey(datapack.uuid))
        {
            callbackMap[datapack.uuid]?.success(datapack)
            callbackMap.remove(datapack.uuid)
        }

        // 解析数据包，对内容进行处理
        RPCService.analyseDatapack(datapack)
    }

}