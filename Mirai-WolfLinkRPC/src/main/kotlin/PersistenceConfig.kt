package org.wolflink.mirai.wolflinkrpc

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel

object PersistenceConfig : AutoSavePluginData("wolflinkrpc_config") {

    val enabledGroups : MutableSet<Long> by value(mutableSetOf(332607573L))
    val botManagers : MutableSet<Long> by value(mutableSetOf(3401286177L,694163437L))
    val permissionMap : MutableMap<String,PermissionLevel> by value(mutableMapOf("测试用户的uniqueID" to PermissionLevel.DEFAULT))
    var debug : Boolean by value(false)
    var host : String by value("127.0.0.1")
    var port : Int by value(10000)
    var username : String by value("username")
    var password : String by value("password")
    var clientTag : String by value("default")
}