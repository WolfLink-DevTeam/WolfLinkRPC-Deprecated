package org.wolflink.mirai.wolflinkrpc.handler.remote

import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.mirai.wolflinkrpc.data.MemberData

@RemoteCallHandler
object BirthdayVerify : SimpleRemoteHandler() {

    override fun getKeyword(): String = "验证生日"

    override fun getAction(): IAction {
        return object : IAction {
            override fun invoke(datapack: RPCDataPack) {
                MQService.sendCommandFeedBack(datapack,SimpleCommandResultBody(MemberData.verifyBirthday(datapack.sender.userName),""))
            }
        }
    }
}