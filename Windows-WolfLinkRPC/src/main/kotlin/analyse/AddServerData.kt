package org.wolflink.windows.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.windows.wolflinkrpc.PersistenceCfg
import org.wolflink.windows.wolflinkrpc.entity.MCServer
import org.wolflink.windows.wolflinkrpc.entity.MCServerDataClass

@AnalyseFunction
class AddServerData : SimpleCommandAnalyse() {

    override fun getPermission(): PermissionLevel = PermissionLevel.ADMIN
    override fun getKeyword(): String = "添加服务器数据"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val originCommand = datapack.jsonObject["command"].asString
                val command = getCommand(originCommand)
                val commandParts = command.split("||")
                if(commandParts.size < 4)
                {
                    MQService.sendCommandFeedBack(datapack,
                        SimpleCommandResultBody(false,
                            "指令格式错误,可参考格式: \n添加服务器数据 {服务器名称}||{服务器标题}||{服务端文件夹}||{启动脚本名(带后缀)}")
                    )
                    return
                }
                PersistenceCfg.addMCServerData(MCServer(MCServerDataClass(commandParts[0],commandParts[1],commandParts[2],commandParts[3])))
                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(true,"服务器数据添加完成")
                )
                return
            }
        }
    }
}