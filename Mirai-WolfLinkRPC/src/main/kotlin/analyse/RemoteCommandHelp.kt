package org.wolflink.mirai.wolflinkrpc.analyse

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.entity.impl.analyse.RemoteCommandHelpImpl

@AnalyseFunction
class RemoteCommandHelp : RemoteCommandHelpImpl("""
    帮助 - 查询远程指令帮助
    设置权限 {用户uniqueID} {权限名} - 权限等级必须低于发起者
    修改群号 {目标群号}
    查询目录 {文件夹路径} - 用|符号作为反斜杠的替代符
    个人信息 - 查询当前数据包发送者的信息
""".trimIndent()) {
}