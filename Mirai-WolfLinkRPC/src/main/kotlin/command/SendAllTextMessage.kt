package org.wolflink.mirai.wolflinkrpc.command

import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleTextMessageBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.mirai.wolflinkrpc.RPCConfiguration

@CommandFunction
class SendAllTextMessage : ICommandFunction {
    override fun getCommand(): String = "> 广播消息"

    override fun getPermission(): PermissionLevel = PermissionLevel.ADMIN
    override fun invoke(sender : ISender, args: List<String>): Boolean {
        val message = args.joinToString(" ")
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleTextMessageBody(message))
            .setSender(sender)
            .addRoutingData(RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all")))
            .setType(DataPackType.TEXT_MESSAGE)
            .build()
        MQService.sendDataPack(datapack)
        return true
    }
}