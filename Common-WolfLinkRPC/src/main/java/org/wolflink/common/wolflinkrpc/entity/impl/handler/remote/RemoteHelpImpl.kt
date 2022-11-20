package org.wolflink.common.wolflinkrpc.entity.impl.handler.remote

import org.wolflink.common.wolflinkrpc.BaseConfiguration
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService

open class RemoteHelpImpl(val helpText : String) : SimpleRemoteHandler() {
    override fun getKeyword(): String = "帮助"
    override fun getAction(): IAction {
        return object : IAction {
            override fun invoke(datapack: RPCDataPack) {
                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(
                        false,
                        "-\n${BaseConfiguration.projectChineseName} 远程指令帮助 - ${RPCCore.configuration.getClientType().name}\n-\n$helpText\n-")
                )
            }
        }
    }
}