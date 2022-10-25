package org.wolflink.windows.wolflinkrpc.analyse


import com.google.gson.Gson
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleSender
import org.wolflink.windows.wolflinkrpc.RPCLogger

@AnalyseFunction
class BroadcastTextMessage : IAnalyse {
    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val sender = Gson().fromJson(datapack.jsonObject.get("sender").asString,SimpleSender::class.java)
                val msg = datapack.jsonObject.get("msg").asString
                if(sender.getPlatform() != ClientType.ANONYMOUS) RPCLogger.info("${datapack.senderName} | ${sender.getName()} >> $msg")
                else RPCLogger.info(msg)
            }
        }
    }
    override fun getPredicate(): IPredicate {
        return object : IPredicate{
            override fun invoke(datapack: RPCDataPack): Boolean = (datapack.type == DataPackType.TEXT_MESSAGE)
        }
    }
}