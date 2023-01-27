package org.wolflink.mirai.wolflinkrpc.handler.local.memberinfo

import org.wolflink.common.wolflinkrpc.api.annotations.LocalCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ILocalHandler
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.mirai.wolflinkrpc.data.MemberData

@LocalCallHandler
object MemberInfoQuery : ILocalHandler {
    override fun getCommand(): String = "> 个人信息"

    override fun invoke(sender: RPCUser, args: List<String>): Pair<Boolean, String> {
        if(sender.clientType != ClientType.MIRAI)return Pair(false,"只能在 QQ机器人 环境下使用该功能。")
        val uniqueID = sender.uniqueID.toLong()
        val mcID = MemberData.gameNameMap.getOrDefault(uniqueID,"暂未绑定")
        val birthData = MemberData.birthdayMap.get(uniqueID)
        val birthDay = if(birthData == null)"暂未绑定" else "${birthData.second}月${birthData.third}日"

        return Pair(true,"""
            -
            绫狼互联 - WolfBot+
            -
            唯一标识 $uniqueID
            MCID $mcID
            生日 $birthDay
            -
        """.trimIndent())
    }
}