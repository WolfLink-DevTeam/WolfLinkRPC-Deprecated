package org.wolflink.mirai.wolflinkrpc

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.service.CommandService
import org.wolflink.common.wolflinkrpc.service.RPCService
import org.wolflink.mirai.wolflinkrpc.analyse.*
import org.wolflink.mirai.wolflinkrpc.command.*
import org.wolflink.mirai.wolflinkrpc.listener.BotLoginListener
import org.wolflink.mirai.wolflinkrpc.listener.GroupMsgListener

object App : KotlinPlugin(
    JvmPluginDescription(
        id = "org.wolflink.mirai.wolflinkrpc",
        name = "WolfLinkRPC",
        version = "1.0.0",
    ) {
author("MikkoAyaka")
    }
) {
    lateinit var bot : Bot
    var enabled = false

    override fun onEnable() {
        //监听机器人登录事件，没有登录就不启用
        GlobalEventChannel.registerListenerHost(BotLoginListener)
    }

    override fun onDisable() {
        super.onDisable()
        if(enabled)
        {
            enabled = false
            RPCCore.closeSystem()
        }
    }
    fun enablePlugin(){
        reloadConfig()
        registerListener()

        RPCCore.initSystem(RPCConfiguration)
        CommandService.bindCommand(SendSingleTextMessage())
        CommandService.bindCommand(SendAllTextMessage())
        CommandService.bindCommand(SendGroupTextMessage())
        CommandService.bindCommand(RemoteAPICall())
        CommandService.bindCommand(LocalCommandHelp())
        CommandService.bindCommand(SetLocalPermission())

        RPCService.analyseFunctionList = mutableListOf()
        RPCService.analyseFunctionList.add(BroadcastTextMessage())
        RPCService.analyseFunctionList.add(RemoteCommandHelp())
        RPCService.analyseFunctionList.add(SetRemotePermission())
        RPCService.analyseFunctionList.add(ChangeQQGroup())
        RPCService.analyseFunctionList.add(FileView())
        RPCService.analyseFunctionList.add(QueryPersonalInfo())

        RPCConfiguration.getLogger().info("Mirai 插件已启用")
    }

    private fun registerListener(){
        GlobalEventChannel.registerListenerHost(GroupMsgListener)
    }

    private fun reloadConfig(){
        PersistenceConfig.reload()
    }
}