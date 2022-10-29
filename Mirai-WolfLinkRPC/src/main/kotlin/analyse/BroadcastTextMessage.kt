package org.wolflink.mirai.wolflinkrpc.analyse

import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.mirai.wolflinkrpc.App
import org.wolflink.mirai.wolflinkrpc.PersistenceConfig

@AnalyseFunction
class BroadcastTextMessage : IAnalyse {
    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                for (groupID in PersistenceConfig.enabledGroups)
                {
                    GlobalScope.launch {
                        val sender = datapack.sender
                        val msg = datapack.jsonObject.get("msg").asString
                        if(sender.getPlatform() != ClientType.ANONYMOUS) App.bot.getGroup(groupID)?.sendMessage("[${datapack.sender.getQueueName()}|${sender.getUserName()}] $msg")
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