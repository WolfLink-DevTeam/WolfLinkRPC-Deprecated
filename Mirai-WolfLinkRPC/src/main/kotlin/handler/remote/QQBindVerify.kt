package org.wolflink.mirai.wolflinkrpc.handler.remote

import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.mirai.wolflinkrpc.data.MemberData

@RemoteCallHandler
object QQBindVerify : SimpleRemoteHandler() {

    override fun getKeyword(): String = "验证绑定"

    override fun getAction(): IAction {
        return object : IAction {
            override fun invoke(datapack: RPCDataPack) {

                val result = MemberData.getQQNumber(datapack.sender.userName) != 0L

                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(result,"")
                )
            }
        }
    }
}