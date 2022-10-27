package org.wolflink.mirai.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.entity.impl.analyse.RemoteCommandHelpImpl

@AnalyseFunction
class RemoteCommandHelp : RemoteCommandHelpImpl("""
    帮助 - 查询远程指令帮助
    设置权限 {用户uniqueID} {权限名} - 权限等级必须低于发起者
""".trimIndent()) {
}