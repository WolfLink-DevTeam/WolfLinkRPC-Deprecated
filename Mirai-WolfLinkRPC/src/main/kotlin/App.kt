package org.wolflink.mirai.wolflinkrpc

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody
import org.wolflink.common.wolflinkrpc.entity.CommandData
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.service.MQService.sendDataPack
import org.wolflink.common.wolflinkrpc.service.RPCService
import org.wolflink.mirai.wolflinkrpc.analyse.BroadcastTextMessage
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
        initEnabledGroups()
        initBotManagers()
        registerListener()

        RPCCore.initSystem(RPCConfiguration)
        CommandData.bindCommand(SendSingleTextMessage())
        CommandData.bindCommand(SendAllTextMessage())
        CommandData.bindCommand(SendGroupTextMessage())
        CommandData.bindCommand(BukkitAPICall())

        RPCService.analyseFunctionList = mutableListOf()
        RPCService.analyseFunctionList.add(BroadcastTextMessage())

        RPCConfiguration.getLogger().info("Mirai 插件已启用")
    }
    private fun initEnabledGroups(){
        PersistenceConfig.enabledGroups.add(332607573L)
        //PersistenceConfig.enabledGroups.add(152919430L)
    }
    private fun initBotManagers(){
        PersistenceConfig.botManagers.add(3401286177L)
        PersistenceConfig.botManagers.add(694163437L)
    }

    private fun registerListener(){
        GlobalEventChannel.registerListenerHost(GroupMsgListener)
    }

    private fun reloadConfig(){
        PersistenceConfig.reload()
    }
}