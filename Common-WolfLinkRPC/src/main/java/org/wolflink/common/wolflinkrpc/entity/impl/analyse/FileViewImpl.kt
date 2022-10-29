package org.wolflink.common.wolflinkrpc.entity.impl.analyse

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import java.io.File

open class FileViewImpl : SimpleCommandAnalyse() {

    override fun getPermission(): PermissionLevel = PermissionLevel.OWNER

    override fun getKeyword(): String = "查询目录"

    override fun getAction(): IAction {
        return object : IAction{
            override fun invoke(datapack: RPCDataPack) {
                val originCommand = datapack.jsonObject["command"].asString
                val filePath = getCommand(originCommand).replace("|","\\")
                val rootFile = File(filePath)
                if(rootFile.exists())
                {
                    val subFiles = rootFile.listFiles()
                    var resultMsg = "-\n文件夹 $filePath 包含以下文件\n-\n"
                    for (file in subFiles)
                    {
                        resultMsg += "${file.name}\n"
                    }
                    MQService.sendCommandFeedBack(datapack,
                        SimpleCommandResultBody(true,resultMsg)
                    )
                }else{
                    MQService.sendCommandFeedBack(datapack,
                        SimpleCommandResultBody(false,"不存在路径为 $filePath 的文件夹")
                    )
                }
            }
        }
    }
}