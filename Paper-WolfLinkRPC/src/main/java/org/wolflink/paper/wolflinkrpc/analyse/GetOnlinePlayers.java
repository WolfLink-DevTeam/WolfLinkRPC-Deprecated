package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;
import org.wolflink.common.wolflinkrpc.entity.RoutingData;
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;

import java.util.List;

@AnalyseFunction
public class GetOnlinePlayers implements IAnalyse {
    @NotNull
    @Override
    public IPredicate getPredicate() {
        return (rpcDataPack) ->  rpcDataPack.getType() == DataPackType.COMMAND_EXECUTE &&
                rpcDataPack.getJsonObject().get("command").getAsString().startsWith("查询在线玩家");
    }

    @NotNull
    @Override
    public IAction getAction() {
        return (rpcDataPack) -> {
            App.RPC_LOGGER.info("正在执行指令 查询在线玩家");
            StringBuilder onlinePlayers = new StringBuilder("在线玩家 "+ Bukkit.getOnlinePlayers().size()+" 人\n");
            for (Player p : Bukkit.getOnlinePlayers())
            {
                onlinePlayers.append(p.getName());
                onlinePlayers.append(" ");
            }
            RPCDataPack datapack = new RPCDataPack.Builder()
                    .setDatapackBody(new SimpleCommandResultBody(true,onlinePlayers.toString()))
                    .setType(DataPackType.COMMAND_RESULT)
                    .setSenderName(App.RPC_CONFIGURATION.getQueueName())
                    .addRoutingData(new RoutingData(ExchangeType.SINGLE_EXCHANGE, List.of(rpcDataPack.getSenderName())))
                    .setUUID(rpcDataPack.getUuid())
                    .build();
            MQService.INSTANCE.sendDataPack(datapack);
        };
    }
}
