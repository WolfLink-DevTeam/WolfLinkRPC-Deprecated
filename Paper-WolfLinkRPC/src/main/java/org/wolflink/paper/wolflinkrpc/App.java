package org.wolflink.paper.wolflinkrpc;

import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.RPCCore;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType;
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger;
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;
import org.wolflink.common.wolflinkrpc.entity.RoutingData;
import org.wolflink.common.wolflinkrpc.service.MQService;

import java.util.List;

public final class App extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    public static RPCConfiguration RPC_CONFIGURATION;
    public static ILogger RPC_LOGGER;
    @Override
    public void onEnable() {
        INSTANCE = this;
        RPC_CONFIGURATION = new RPCConfiguration();
        RPC_LOGGER = new RPCLogger();
        this.saveDefaultConfig();
        // Plugin startup logic
        RPCCore.INSTANCE.initSystem(RPC_CONFIGURATION);

        sendOnlineMessage();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        RPCCore.INSTANCE.closeSystem();
    }
    private void sendOnlineMessage(){
        ITextMessageBody textMessageBody = new ITextMessageBody() {
            @NotNull
            @Override
            public JsonObject toJsonObject() {
                return ITextMessageBody.DefaultImpls.toJsonObject(this);
            }
            @NotNull
            @Override
            public String getMsg() {
                return "用户端 "+RPC_CONFIGURATION.getQueueName()+" 已上线";
            }
        };
        RPCDataPack rpcDataPack = new RPCDataPack.Builder()
                .setDatapackBody(textMessageBody)
                .setSenderName(RPC_CONFIGURATION.getQueueName())
                .setType(DataPackType.TEXT_MESSAGE)
                .addRoutingData(new RoutingData(ExchangeType.ALL_EXCHANGE, List.of("broadcast.all")))
                .build();
        MQService.INSTANCE.sendDataPack(rpcDataPack);
    }
}
