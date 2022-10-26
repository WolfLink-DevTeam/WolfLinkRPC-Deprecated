package org.wolflink.common.wolflinkrpc.entity.impl.analyse

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.common.wolflinkrpc.service.PermissionService

class SetPermissionImpl : SimpleCommandAnalyse() {
    override fun getKeyword(): String = "设置权限"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val permission = datapack.sender.getPermission()
                val originCommand = datapack.jsonObject["command"].asString
                val command = getCommand(originCommand)
                val commandParts = command.split(" ")
                if(commandParts.size != 2){
                    MQService.sendCommandFeedBack(datapack,SimpleCommandResultBody(false,"指令格式错误，参考格式：设置权限 {用户唯一ID} API_USER"))
                    return
                }
                // 为用户设置权限
                try {
                    PermissionService.permissionGroupMap[commandParts[0]] = PermissionLevel.valueOf(commandParts[1].uppercase())
                    MQService.sendCommandFeedBack(datapack,SimpleCommandResultBody(true,"权限设置成功，用户 ${commandParts[0]} 当前权限 ${commandParts[1].uppercase()}"))
                } catch (e : Exception)
                {
                    MQService.sendCommandFeedBack(datapack,SimpleCommandResultBody(false,"权限设置失败！异常：${e.message ?: "未知"}"))
                }

            }
        }
    }
}