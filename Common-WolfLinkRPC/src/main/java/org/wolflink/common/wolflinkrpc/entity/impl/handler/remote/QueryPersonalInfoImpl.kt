package org.wolflink.common.wolflinkrpc.entity.impl.handler.remote

import org.wolflink.common.wolflinkrpc.BaseConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService

open class QueryPersonalInfoImpl : SimpleRemoteHandler() {
    override fun getKeyword(): String = "个人信息"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {

                val sender = datapack.sender

                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(true,"""
                        -
                        ${BaseConfiguration.projectChineseName} 个人信息
                        -
                        用户名称 - ${sender.userName}
                        特征编码 - ${sender.uniqueID}
                        权限等级 - ${sender.getPermission().name.uppercase()} | ${sender.getPermission().level}
                        路由队列 - ${sender.queueName}
                        客户端 - ${sender.clientType.name.uppercase()}
                        -
                    """.trimIndent())
                )
            }
        }
    }
}