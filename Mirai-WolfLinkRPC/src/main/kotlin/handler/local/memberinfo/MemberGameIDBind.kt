package org.wolflink.mirai.wolflinkrpc.handler.local.memberinfo

import org.wolflink.common.wolflinkrpc.api.annotations.LocalCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ILocalHandler
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.mirai.wolflinkrpc.data.MemberData

@LocalCallHandler
object MemberGameIDBind : ILocalHandler {
    override fun getCommand(): String = "> MCID 绑定"

    override fun invoke(sender: RPCUser, args: List<String>): Pair<Boolean, String> {
        if(sender.clientType != ClientType.MIRAI)return Pair(false,"只能在 QQ机器人 环境下使用该功能。")
        if(args.isNotEmpty())
        {
            val id = args[0]
            if(MemberData.gameNameMap.containsKey(sender.uniqueID.toLong()))return Pair(false,"已经绑定过MC-ID了！")
            MemberData.gameNameMap[sender.uniqueID.toLong()] = id
            return Pair(true,"绑定成功")
        }
        else return Pair(false,"参数为空！")
    }
}