package org.wolflink.mirai.wolflinkrpc.command

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandExecuteBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.mirai.wolflinkrpc.App
import org.wolflink.mirai.wolflinkrpc.PersistenceConfig

@CommandFunction
class RemoteAPICall : ICommandFunction {
    //示例 > 接口调用 paper_1 查询在线玩家
    //示例 > 接口调用 paper_1 bukkit指令 op MikkoAyaka
    override fun getCommand(): String = "> 接口调用"
    override fun getPermission(): PermissionLevel = PermissionLevel.API_USER

    override fun invoke(sender : ISender, args: List<String>): Boolean {
        if(args.size < 2)return false
        val routingKey = args[0]
        val command = args.subList(1,args.size).joinToString(" ")
        val datapack = RPCDataPack.Builder()
            .setDatapackBody(SimpleCommandExecuteBody(command))
            .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(routingKey)))
            .setType(DataPackType.COMMAND_EXECUTE)
            .setSender(sender)
            .build()
        val callbackFunction = object : CallbackFunction{
            override fun success(datapack: RPCDataPack) {
                for (groupID in PersistenceConfig.enabledGroups)
                {
                    GlobalScope.launch{
                        App.bot.getGroup(groupID)?.sendMessage(datapack.jsonObject.get("info").asString)
                    }
                }
            }
            override fun failed(datapack: RPCDataPack) {
                for (groupID in PersistenceConfig.enabledGroups)
                {
                    GlobalScope.launch{
                        App.bot.getGroup(groupID)?.sendMessage("响应超时，未能在指定时间内接收到来自客户端的回应。")
                    }
                }
            }
        }
        MQService.sendDataPack(datapack,true,callbackFunction,10)
        return true
    }
}