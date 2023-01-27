package org.wolflink.mirai.wolflinkrpc.handler.local.memberinfo

import org.wolflink.common.wolflinkrpc.api.annotations.LocalCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ILocalHandler
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.mirai.wolflinkrpc.data.MemberData

@LocalCallHandler
object MemberBirthdayBind : ILocalHandler {
    override fun getCommand(): String = "> 生日 绑定"

    override fun invoke(sender: RPCUser, args: List<String>): Pair<Boolean, String> {
        if(sender.clientType != ClientType.MIRAI)return Pair(false,"只能在 QQ机器人 环境下使用该功能。")
        if(args.isNotEmpty())
        {
            val year = args.getOrNull(0)?.toIntOrNull()
            val month = args.getOrNull(1)?.toIntOrNull()
            val day = args.getOrNull(2)?.toIntOrNull()
            if(year != null && month != null && day != null)
            {
                if(MemberData.birthdayMap.containsKey(sender.uniqueID.toLong()))return Pair(false,"已经绑定过生日了！")
                MemberData.birthdayMap[sender.uniqueID.toLong()] = Triple(year,month,day)
                return Pair(true,"绑定成功")
            }
            else
            {
                return Pair(false,"请输入正确的生日，如 2000 11 22 以空格隔开")
            }
        }
        else return Pair(false,"参数为空！")
    }
}