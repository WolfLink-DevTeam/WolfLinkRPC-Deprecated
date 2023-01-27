package org.wolflink.mirai.wolflinkrpc.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object MemberData : AutoSavePluginData("member_data") {

    val birthdayMap : MutableMap<Long,Triple<Int,Int,Int>> by value()

    val gameNameMap : MutableMap<Long,String> by value()

}