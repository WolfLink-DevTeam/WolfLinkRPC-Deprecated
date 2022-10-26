package org.wolflink.windows.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import java.io.IOException

@AnalyseFunction
class RunWindowsFile : SimpleCommandAnalyse() {

    override fun getPermission(): PermissionLevel = PermissionLevel.OWNER
    override fun getKeyword(): String = "运行文件"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val originCommand = datapack.jsonObject["command"].asString
                val batPath = getCommand(originCommand).replace("|","\\")

                val diskChar = batPath.first()
                val bodyPath = batPath.substring(0,batPath.lastIndexOf('\\'))
                val batName = batPath.substring(batPath.lastIndexOf('\\')+1)
                val command = "cmd /c cd $bodyPath & $diskChar: & start $batName"
                try {
                    Runtime.getRuntime().exec(command)
                } catch (e : IOException)
                {
                    MQService.sendCommandFeedBack(datapack,
                        SimpleCommandResultBody(false,
                            "在运行文件的过程中遇到了问题\n详细语句: $command")
                    )
                    return
                }
                MQService.sendCommandFeedBack(datapack,
                    SimpleCommandResultBody(true,
                        "文件运行成功")
                )
            }
        }
    }
}