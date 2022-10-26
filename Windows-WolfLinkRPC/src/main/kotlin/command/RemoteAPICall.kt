package org.wolflink.windows.wolflinkrpc.command

import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.command.RemoteAPICallImpl
import org.wolflink.windows.wolflinkrpc.RPCLogger

@CommandFunction
class RemoteAPICall : RemoteAPICallImpl(object : CallbackFunction{
    override fun success(datapack: RPCDataPack) {
        RPCLogger.info(datapack.jsonObject.get("info").asString)
    }
    override fun failed(datapack: RPCDataPack) {
        RPCLogger.info("响应超时，未能在指定时间内接收到来自客户端的回应。")
    }
})