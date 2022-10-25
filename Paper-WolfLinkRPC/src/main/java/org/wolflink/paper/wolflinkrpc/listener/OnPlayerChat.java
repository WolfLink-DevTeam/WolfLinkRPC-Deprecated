package org.wolflink.paper.wolflinkrpc.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleSender;
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
            if(RPCService.INSTANCE.analyseCommand(new SimpleSender(p.getName(),p.getUniqueId().toString(), App.RPC_CONFIGURATION.getClientType()),msg))
            {
                p.sendMessage(App.RPC_CONFIGURATION.getProjectChineseName(false)+" \u00a7f指令执行成功");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,2);
            }
            else
            {
                p.sendMessage(App.RPC_CONFIGURATION.getProjectChineseName(false)+" \u00a7c指令执行失败");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            }
        }
    }
}
