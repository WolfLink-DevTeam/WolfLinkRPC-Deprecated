package org.wolflink.mirai.wolflinkrpc.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import java.util.Calendar

object MemberData : AutoSavePluginData("member_data") {

    val birthdayMap : MutableMap<Long,Triple<Int,Int,Int>> by value()

    val gameNameMap : MutableMap<Long,String> by value()

    fun getQQNumber(mcName : String) : Long
    {
        for (entry in gameNameMap.entries)
        {
            if(entry.value.equals(mcName,true))return entry.key
        }
        return 0L
    }
    fun verifyBirthday(mcName: String) : Boolean
    {
        val qqNumber = getQQNumber(mcName)
        if(qqNumber == 0L)return false
        return verifyBirthday(qqNumber)
    }
    fun verifyBirthday(qqNumber: Long) : Boolean
    {
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val birthday = birthdayMap[qqNumber] ?: return false
        if(month == birthday.second && day == birthday.third)return true
        return false
    }

}