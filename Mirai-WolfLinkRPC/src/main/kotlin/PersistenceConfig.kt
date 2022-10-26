package org.wolflink.mirai.wolflinkrpc

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel

object PersistenceConfig : AutoSavePluginData("wolflinkrpc_config") {

    val enabledGroups : MutableSet<Long> by value()

    val botManagers : MutableSet<Long> by value()

    val permissionMap : MutableMap<String,PermissionLevel> by value()

}