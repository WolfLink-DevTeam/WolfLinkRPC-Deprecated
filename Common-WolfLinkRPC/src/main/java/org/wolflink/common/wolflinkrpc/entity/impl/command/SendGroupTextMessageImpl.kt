package org.wolflink.common.wolflinkrpc.entity.impl.command

import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleTextMessageBody
import org.wolflink.common.wolflinkrpc.service.MQService

open class SendGroupTextMessageImpl : ICommandFunction {
    override fun getCommand(): String = "> 小组消息"
    override fun getPermission(): PermissionLevel = PermissionLevel.ADMIN
    override fun invoke(sender : ISender, args: List<String>): Boolean {
        if(args.size < 2)return false
        val routingKey = args[0]
        val message = args.subList(1,args.size).joinToString(" ")
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleTextMessageBody(message))
            .setSender(sender)
            .addRoutingData(RoutingData(ExchangeType.GROUP_EXCHANGE, mutableListOf("broadcast.$routingKey")))
            .setType(DataPackType.TEXT_MESSAGE)
            .build()
        MQService.sendDataPack(datapack)
        return true
    }
}