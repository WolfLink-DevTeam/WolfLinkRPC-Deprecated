package org.wolflink.mirai.wolflinkrpc

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object PersistenceConfig : AutoSavePluginData("wolflinkrpc_config") {

    val enabledGroups : MutableSet<Long> by value()

    val botManagers : MutableSet<Long> by value()

}