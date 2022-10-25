package org.wolflink.mirai.wolflinkrpc.listener

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.BotActiveEvent
import org.wolflink.mirai.wolflinkrpc.App

object BotLoginListener : ListenerHost {
    @EventHandler
    suspend fun BotActiveEvent.onLogin()
    {
        if(!App.enabled)
        {
            App.enabled = true
            App.bot = bot
            App.enablePlugin()
        }
    }
}