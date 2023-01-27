package org.wolflink.paper.wolflinkrpc.listener;

import kotlin.Pair;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.wolflink.common.wolflinkrpc.entity.RoutingData;
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser;
import org.wolflink.common.wolflinkrpc.service.RPCService;
import org.wolflink.paper.wolflinkrpc.App;

public class OnPlayerChat implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        if(e.isCancelled())return;
        String msg = e.getMessage();
        if(msg.startsWith(">"))
        {
            e.setCancelled(true);//阻止消息发送到公屏
            Player p = e.getPlayer();
            p.sendMessage(App.RPC_CONFIGURATION.getProjectChineseName(false)+" \u00a7f执行本地指令 §8» §a"+msg);
            Pair<Boolean,String> ktPair = RPCService.INSTANCE.analyseCommand(new RPCUser(App.RPC_CONFIGURATION.getQueueName(),App.RPC_CONFIGURATION.getClientType(),p.getName(),p.getUniqueId().toString(), new RoutingData()),msg);
            if(ktPair.getFirst())
            {
                p.sendMessage(App.RPC_CONFIGURATION.getProjectChineseName(false)+" \u00a7f"+ktPair.getSecond());
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,2);
            }
            else
            {
                p.sendMessage(App.RPC_CONFIGURATION.getProjectChineseName(false)+" \u00a7c"+ktPair.getSecond());
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            }
        }
    }
}
