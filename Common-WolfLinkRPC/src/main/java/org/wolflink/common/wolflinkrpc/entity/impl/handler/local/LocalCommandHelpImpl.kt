package org.wolflink.common.wolflinkrpc.entity.impl.handler.local

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ILocalHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleTextMessageBody
import org.wolflink.common.wolflinkrpc.entity.role.AnonymousUser
import org.wolflink.common.wolflinkrpc.entity.role.ClientReceiver
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.common.wolflinkrpc.service.CommandService
import org.wolflink.common.wolflinkrpc.service.MQService

open class LocalCommandHelpImpl : ILocalHandler {
    override fun getCommand(): String = "> 帮助"

    override fun invoke(sender: RPCUser, args: List<String>): Pair<Boolean,String> {

        //向自己发送匿名数据包

        val routingKey = RPCCore.configuration.getQueueName()
        val command = args.joinToString(" ")
        val message = CommandService.listSubCommand(command).joinToString("\n") { "${it.commandText} 权限 ${it.permission.name}" }
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleTextMessageBody("指令 $command 拥有以下子指令\n$message"))
            .addReceiver(ClientReceiver(routingKey))
            .setSender(AnonymousUser(routingKey))
            .setType(DataPackType.TEXT_MESSAGE)
            .build()
        MQService.sendDataPack(datapack)
        return Pair(true,"Command executed successfully .")
    }
}