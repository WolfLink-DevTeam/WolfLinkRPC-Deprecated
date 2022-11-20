package org.wolflink.mirai.wolflinkrpc.handler.remote

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IRemoteHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.mirai.wolflinkrpc.App
import org.wolflink.mirai.wolflinkrpc.PersistenceConfig

@RemoteCallHandler
class BroadcastTextMessage : IRemoteHandler {
    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                for (groupID in PersistenceConfig.enabledGroups)
                {
                    GlobalScope.launch {
                        val sender = datapack.sender
                        val msg = datapack.jsonObject.get("msg").asString
                        if(sender.clientType != ClientType.ANONYMOUS) App.bot.getGroup(groupID)?.sendMessage("[${datapack.sender.queueName}|${sender.userName}] $msg")
                        else App.bot.getGroup(groupID)?.sendMessage(msg)
                    }
                }
            }
        }
    }
    override fun getPredicate(): IPredicate {
        return object : IPredicate{
            override fun invoke(datapack: RPCDataPack): Boolean = (datapack.type == DataPackType.TEXT_MESSAGE && App.enabled)
        }
    }
}