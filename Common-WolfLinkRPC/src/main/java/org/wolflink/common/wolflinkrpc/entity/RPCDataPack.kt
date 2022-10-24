package org.wolflink.common.wolflinkrpc.entity

import com.google.gson.JsonObject
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.JsonSerializable
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.IDataPackBody
import org.wolflink.common.wolflinkrpc.service.MQService
import java.util.UUID


data class RPCDataPack(
    var senderName : String = MQService.queueName, // 发送者用户名称
    var type : DataPackType = DataPackType.TEXT_MESSAGE, // 数据包功能类型
    var jsonObject : JsonObject = JsonObject(), // 数据包内容
    var uuid: UUID = UUID.randomUUID(), // 数据包唯一ID
    val routingDataList: MutableList<RoutingData> = mutableListOf() //路由数据
                       )
{
    companion object : JsonSerializable<RPCDataPack>

    fun addRoutingData(routingData: RoutingData)
    {
        routingDataList.add(routingData)
    }

    class Builder
    {
        private var senderName : String? = null
        private var type : DataPackType? = null
        private var datapackBody : IDataPackBody? = null
        private var uuid : UUID? = null
        private var routingDataList : MutableList<RoutingData> = mutableListOf()

        fun setSenderName(senderName: String) : Builder
        {
            this.senderName = senderName
            return this
        }
        fun setType(type: DataPackType) : Builder
        {
            this.type = type
            return this
        }
        fun setDatapackBody(datapackBody : IDataPackBody) : Builder
        {
            this.datapackBody = datapackBody
            return this
        }
        fun setUUID(uuid: UUID) : Builder
        {
            this.uuid = uuid
            return this
        }
        fun addRoutingData(routingData: RoutingData) : Builder
        {
            routingDataList.add(routingData)
            return this
        }
        fun build() : RPCDataPack
        {
            return RPCDataPack(
                senderName ?: MQService.queueName,
                type ?: DataPackType.TEXT_MESSAGE,
                datapackBody?.toJsonObject() ?: JsonObject(),
                uuid ?: UUID.randomUUID(),
                routingDataList
            )
        }
    }
}