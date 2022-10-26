package org.wolflink.windows.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.windows.wolflinkrpc.PersistenceCfg

@AnalyseFunction
class ManagerMCServer : SimpleCommandAnalyse() {

    override fun getPermission(): PermissionLevel = PermissionLevel.OWNER
    override fun getKeyword(): String = "管理MC服务器"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val command = getCommand(datapack.jsonObject.get("command").asString)
                val commandParts = command.split(" ")
                if(commandParts.isEmpty())
                {
                    MQService.sendCommandFeedBack(datapack,
                        SimpleCommandResultBody(false,"指令缺少参数,可参考格式: \n管理MC服务器 查询所有")
                    )
                    return
                }
                if(commandParts.size == 1)
                {
                    if(commandParts[0] == "查询所有")
                    {
                        var message = """
                                        
                                        服务器列表
                                        [名称]-[运行状态]-[运行时间]
                                        
                                    """.trimIndent()
                        for (serverData in PersistenceCfg.mcServerDataMap.values)
                        {
                            message += "${serverData.data.name} ${if(serverData.isRunning)"运行中 ${serverData.getRunTimeString()}" else "未处在运行中"}\n"
                        }
                        MQService.sendCommandFeedBack(datapack,
                            SimpleCommandResultBody(true,message)
                        )
                    }
                    else
                    {
                        MQService.sendCommandFeedBack(datapack,
                            SimpleCommandResultBody(false,"指令参数不正确,可参考格式: 管理MC服务器 查询所有")
                        )
                    }
                    return
                }
                if(commandParts.size == 2)
                {
                    if(commandParts[0] == "启动")
                    {
                        val mcServer = PersistenceCfg.getMCServerData(commandParts[1])
                        if(mcServer == null)
                        {
                            MQService.sendCommandFeedBack(datapack,
                                SimpleCommandResultBody(false,"未找到服务器 ${commandParts[1]}")
                            )
                            return
                        }
                        val result = mcServer.startServer()
                        MQService.sendCommandFeedBack(datapack,
                            SimpleCommandResultBody(result.first,result.second)
                        )
                        return
                    }
                    if(commandParts[0] == "关闭")
                    {
                        val mcServer = PersistenceCfg.getMCServerData(commandParts[1])
                        if(mcServer == null)
                        {
                            MQService.sendCommandFeedBack(datapack,
                                SimpleCommandResultBody(false,"未找到服务器 ${commandParts[1]}")
                            )
                            return
                        }
                        val result = mcServer.stopServer()
                        MQService.sendCommandFeedBack(datapack,
                            SimpleCommandResultBody(result.first,result.second)
                        )
                        return
                    }
                }
                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(false,"指令参数不正确,可参考格式: 管理MC服务器 启动 {服务器名}")
                )
            }
        }
    }
}