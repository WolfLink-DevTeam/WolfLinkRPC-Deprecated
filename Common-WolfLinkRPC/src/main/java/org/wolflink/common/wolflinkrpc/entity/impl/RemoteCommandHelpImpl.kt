package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.BaseConfiguration
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.service.MQService

open class RemoteCommandHelpImpl(val helpText : String) : SimpleCommandAnalyse() {
    override fun getKeyword(): String = "帮助"

    override fun getAction(): IAction {
        return object : IAction {
            override fun invoke(datapack: RPCDataPack) {
                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(
                        false,
                        "-\n${BaseConfiguration.projectChineseName} 远程指令帮助 - ${RPCCore.configuration.getClientType().name}\n-\n$helpText\n-"))
            }
        }
    }
}