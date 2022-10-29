package org.wolflink.common.wolflinkrpc.entity

import com.google.gson.JsonObject
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.JsonSerializable
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.IDataPackBody
import org.wolflink.common.wolflinkrpc.entity.role.ConsoleUser
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.common.wolflinkrpc.service.MQService
import java.util.UUID


data class RPCDataPack(
    var uuid: UUID = UUID.randomUUID(), // 数据包唯一ID
    var type : DataPackType = DataPackType.TEXT_MESSAGE, // 数据包功能类型
    var sender : RPCUser = ConsoleUser(MQService.queueName,RPCCore.configuration.getClientType()), // 发送者用户名称
    var receivers : List<RPCUser>, // 接收者数据
    var jsonObject : JsonObject // 数据包内容
                       )
{
    companion object : JsonSerializable<RPCDataPack>
    class Builder
    {
        private var sender : RPCUser? = null
        private var receivers : MutableList<RPCUser> = mutableListOf()
        private var type : DataPackType? = null
        private var datapackBody : IDataPackBody? = null
        private var uuid : UUID? = null
        private var routingDataList : MutableList<RoutingData> = mutableListOf()

        fun setSender(sender: RPCUser) : Builder
        {
            this.sender = RPCUser(sender.queueName,sender.clientType,sender.userName,sender.uniqueID)
            return this
        }
        fun addReceiver(receiver : RPCUser) : Builder
        {
            receivers.add(RPCUser(receiver.queueName,receiver.clientType,receiver.userName,receiver.uniqueID))
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
        fun build() : RPCDataPack
        {
            return RPCDataPack(
                uuid ?: UUID.randomUUID(),
                type ?: DataPackType.TEXT_MESSAGE,
                sender ?: ConsoleUser(MQService.queueName,RPCCore.configuration.getClientType()),
                receivers,
                datapackBody?.toJsonObject() ?: JsonObject()
            )
        }
    }
}