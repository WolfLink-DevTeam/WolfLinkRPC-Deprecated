package org.wolflink.mirai.wolflinkrpc.listener

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import org.wolflink.common.wolflinkrpc.entity.role.SimpleSender
import org.wolflink.common.wolflinkrpc.service.RPCService
import org.wolflink.mirai.wolflinkrpc.RPCConfiguration

object GroupMsgListener : ListenerHost {
    @EventHandler
    suspend fun GroupMessageEvent.onMessage()
    {
        val msg = this.message.content
        if(msg.startsWith(">"))
        {
            RPCConfiguration.getLogger().info("执行指令")
            RPCService.analyseCommand(SimpleSender(RPCConfiguration.getQueueName(),this.senderName,this.sender.id.toString(),RPCConfiguration.getClientType()),msg)
        }
    }
}