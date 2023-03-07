package org.wolflink.paper.wolflinkrpc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.enums.ClientType;
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;
import org.wolflink.common.wolflinkrpc.entity.RoutingData;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandExecuteBody;
import org.wolflink.common.wolflinkrpc.entity.role.ClientReceiver;
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent e)
    {
        String routingKey = "mirai_WolfBot+";
        String message = "验证绑定";
        Player p = e.getPlayer();
        RPCDataPack datapack = new RPCDataPack.Builder()
                .setDatapackBody(new SimpleCommandExecuteBody(message))
                .setSender(new RPCUser(App.RPC_CONFIGURATION.getQueueName(),
                        ClientType.BUKKIT,
                        p.getName(),
                        p.getName(),
                        new RoutingData()))
                .addReceiver(new ClientReceiver(routingKey))
                .build();
        MQService.INSTANCE.sendDataPack(datapack, true, new CallbackFunction() {
            @Override
            public void success(@NotNull RPCDataPack rpcDataPack) {
                if(!rpcDataPack.getJsonObject().get("result").getAsBoolean())
                {
                    p.kickPlayer("\u00a7cQQ绑定验证失败！\u00a77但网络连接通畅。请联系 \u00a7fQQ 3401286177");
                }
            }
            @Override
            public void failed(@NotNull RPCDataPack rpcDataPack) {
                p.kickPlayer("\u00a7cQQ绑定验证失败！\u00a77可能是网络连接不畅，请重试。多次尝试无果请联系 \u00a7fQQ 3401286177");
            }
        },10);
    }
}
