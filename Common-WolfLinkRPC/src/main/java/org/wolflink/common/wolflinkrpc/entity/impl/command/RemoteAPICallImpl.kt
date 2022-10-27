package org.wolflink.common.wolflinkrpc.entity.impl.command

import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandExecuteBody
import org.wolflink.common.wolflinkrpc.service.MQService

open class RemoteAPICallImpl(private val callbackFunction: CallbackFunction) : ICommandFunction {
    //示例 > 接口调用 paper_1 查询在线玩家
    //示例 > 接口调用 paper_1 bukkit指令 op MikkoAyaka
    override fun getCommand(): String = "> 接口调用"
    override fun getPermission(): PermissionLevel = PermissionLevel.API_USER

    override fun invoke(sender : ISender, args: List<String>): Pair<Boolean,String> {
        if(args.size < 2)return Pair(false,"Not enough arguments .")
        val routingKey = args[0]
        val command = args.subList(1,args.size).joinToString(" ")
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleCommandExecuteBody(command))
            .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(routingKey)))
            .setType(DataPackType.COMMAND_EXECUTE)
            .setSender(sender)
            .build()
        MQService.sendDataPack(datapack,true,callbackFunction,10)
        return Pair(true,"Command executed successfully .")
    }
}