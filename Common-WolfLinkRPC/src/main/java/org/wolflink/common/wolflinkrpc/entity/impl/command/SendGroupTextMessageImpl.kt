package org.wolflink.common.wolflinkrpc.entity.impl.command

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleTextMessageBody
import org.wolflink.common.wolflinkrpc.entity.role.GroupReceiver
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.common.wolflinkrpc.service.MQService

open class SendGroupTextMessageImpl : ICommandFunction {
    override fun getCommand(): String = "> 小组消息"
    override fun getPermission(): PermissionLevel = PermissionLevel.ADMIN
    override fun invoke(sender : RPCUser, args: List<String>): Pair<Boolean,String> {
        if(args.size < 2)return Pair(false,"Not enough arguments .")
        val routingKey = args[0]
        val message = args.subList(1,args.size).joinToString(" ")
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleTextMessageBody(message))
            .setSender(sender)
            .addReceiver(GroupReceiver(routingKey))
            .build()
        MQService.sendDataPack(datapack)
        return Pair(true,"Command executed successfully .")
    }
}