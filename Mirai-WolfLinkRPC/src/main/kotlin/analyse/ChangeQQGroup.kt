package org.wolflink.mirai.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.mirai.wolflinkrpc.PersistenceConfig
import org.wolflink.mirai.wolflinkrpc.RPCConfiguration

@AnalyseFunction
class ChangeQQGroup : SimpleCommandAnalyse() {
    override fun getPermission(): PermissionLevel = PermissionLevel.ADMIN
    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val originCommand: String = datapack.jsonObject.get("command").asString
                val command = getCommand(originCommand)
                val groupNumber = command.toLongOrNull()
                if(groupNumber == null)
                {
                    MQService.sendCommandFeedBack(datapack, SimpleCommandResultBody(false,"请输入正确的群号，必须是纯数字！"))
                    return
                }
                PersistenceConfig.enabledGroups.clear()
                PersistenceConfig.enabledGroups.add(groupNumber)
                MQService.sendCommandFeedBack(datapack, SimpleCommandResultBody(true,"群号修改成功"))
            }
        }
    }

    override fun getKeyword(): String = "修改群号"
}