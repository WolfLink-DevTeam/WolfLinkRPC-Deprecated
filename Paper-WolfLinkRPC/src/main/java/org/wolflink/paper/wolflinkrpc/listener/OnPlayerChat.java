package org.wolflink.paper.wolflinkrpc.listener;

import kotlin.Pair;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.wolflink.common.wolflinkrpc.entity.role.SimpleSender;
import org.wolflink.common.wolflinkrpc.service.RPCService;
import org.wolflink.paper.wolflinkrpc.App;

public class OnPlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        String msg = e.getMessage();
        if(msg.startsWith(">"))
        {
            Player p = e.getPlayer();
            Pair<Boolean,String> ktPair = RPCService.INSTANCE.analyseCommand(new SimpleSender(App.RPC_CONFIGURATION.getQueueName(),p.getName(),p.getUniqueId().toString(), App.RPC_CONFIGURATION.getClientType()),msg);
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
