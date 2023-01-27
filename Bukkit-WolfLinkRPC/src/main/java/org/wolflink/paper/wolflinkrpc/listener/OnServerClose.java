package org.wolflink.paper.wolflinkrpc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.wolflink.common.wolflinkrpc.RPCCore;

public class OnServerClose implements Listener {
    @EventHandler
    public void onServerClose(ServerCommandEvent e)
    {
        if(e.getCommand().equalsIgnoreCase("stop"))
        {
            RPCCore.INSTANCE.closeSystem();
        }
    }

}
