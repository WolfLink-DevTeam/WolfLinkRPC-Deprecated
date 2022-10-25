package org.wolflink.windows.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.ConsoleSender
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.windows.wolflinkrpc.RPCConfiguration
import org.wolflink.windows.wolflinkrpc.enums.ReadableFileType
import java.io.File

@AnalyseFunction
class ReadWindowsFile : SimpleCommandAnalyse() {
    override fun getKeyword(): String = "读取文件"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val originCommand = datapack.jsonObject["command"].asString
                val command = getCommand(originCommand)
                val commandParts = command.split(" ")
                if(commandParts.size < 2)
                {
                    MQService.sendCommandFeedBack(datapack,RPCConfiguration.getQueueName(),
                        SimpleCommandResultBody(ConsoleSender(RPCConfiguration.getQueueName(),RPCConfiguration.getClientType()),false,"指令格式错误,可参考格式: 读取文件 D|Test|myFile txt"))
                    return
                }
                val path = commandParts[0].replace("|",":/")
                val type: ReadableFileType
                try {
                    type = ReadableFileType.valueOf(commandParts[1].uppercase())
                } catch (e : java.lang.IllegalArgumentException)
                {
                    MQService.sendCommandFeedBack(datapack,RPCConfiguration.getQueueName(),
                        SimpleCommandResultBody(ConsoleSender(RPCConfiguration.getQueueName(),RPCConfiguration.getClientType()),false,
                            "文件类型错误,只支持读取以下类型文件: ${ReadableFileType.values().joinToString(" ") { it.name }}"))
                    return
                }
                val file = File("$path.${type.name.lowercase()}")
                if(file.exists() && file.canRead())
                {
                    val text = file.readText()
                    MQService.sendCommandFeedBack(datapack,RPCConfiguration.getQueueName(),
                        SimpleCommandResultBody(ConsoleSender(RPCConfiguration.getQueueName(),RPCConfiguration.getClientType()),true,
                            text))
                    return
                }
                else
                {
                    MQService.sendCommandFeedBack(datapack,RPCConfiguration.getQueueName(),
                        SimpleCommandResultBody(ConsoleSender(RPCConfiguration.getQueueName(),RPCConfiguration.getClientType()),false,
                            "不可操作的文件\n是否存在 ${file.exists()}\n是否可读 ${file.canRead()}\n文件路径 ${file.path}"))
                    return
                }
            }
        }
    }
}