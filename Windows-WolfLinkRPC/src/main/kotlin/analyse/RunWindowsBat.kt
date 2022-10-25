package org.wolflink.windows.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.ConsoleSender
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.windows.wolflinkrpc.RPCConfiguration
import java.io.IOException

@AnalyseFunction
class RunWindowsBat : SimpleCommandAnalyse() {
    override fun getKeyword(): String = "运行脚本"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val originCommand = datapack.jsonObject["command"].asString
                val batPath = getCommand(originCommand).replace("|",":\\")

                val diskChar = batPath.first()
                val bodyPath = batPath.substring(0,batPath.lastIndexOf('\\'))
                val batName = batPath.substring(batPath.lastIndexOf('\\')+1)
                val command = "cmd /c cd $bodyPath & $diskChar: & start $batName"
                try {
                    Runtime.getRuntime().exec(command)
                } catch (e : IOException)
                {
                    MQService.sendCommandFeedBack(datapack, RPCConfiguration.getQueueName(),
                        SimpleCommandResultBody(
                            ConsoleSender(RPCConfiguration.getQueueName(), RPCConfiguration.getClientType()),false,
                            "在运行脚本的过程中遇到了问题\n详细语句: $command")
                    )
                    return
                }
                MQService.sendCommandFeedBack(datapack, RPCConfiguration.getQueueName(),
                    SimpleCommandResultBody(
                        ConsoleSender(RPCConfiguration.getQueueName(), RPCConfiguration.getClientType()),true,
                        "脚本运行成功")
                )
            }
        }
    }
}