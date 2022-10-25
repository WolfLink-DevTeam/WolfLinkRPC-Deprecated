package org.wolflink.mirai.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.entity.impl.RemoteCommandHelpImpl

@AnalyseFunction
class RemoteCommandHelp : RemoteCommandHelpImpl("""
    帮助 - 查询远程指令帮助
""".trimIndent()) {
}