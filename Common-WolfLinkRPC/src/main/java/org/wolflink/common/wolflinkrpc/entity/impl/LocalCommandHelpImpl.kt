package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.CommandData
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.service.MQService

open class LocalCommandHelpImpl : ICommandFunction {
    override fun getCommand(): String = "> 帮助"

    override fun invoke(sender: ISender, args: List<String>): Boolean {

        //向自己发送匿名数据包

        val routingKey = RPCCore.configuration.getQueueName()
        val command = args.joinToString(" ")
        val message = CommandData.listSubCommand(command).joinToString("\n") { it.commandText }
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleTextMessageBody(SimpleSender("","",ClientType.ANONYMOUS),"指令 $command 拥有以下子指令\n$message"))
            .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(routingKey)))
            .setType(DataPackType.TEXT_MESSAGE)
            .setSenderName(routingKey)
            .build()
        MQService.sendDataPack(datapack)
        return true
    }
}