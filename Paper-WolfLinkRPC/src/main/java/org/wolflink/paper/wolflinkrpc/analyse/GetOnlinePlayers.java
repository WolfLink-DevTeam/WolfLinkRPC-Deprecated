package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;
import org.wolflink.common.wolflinkrpc.entity.RoutingData;
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;

import java.util.List;

@AnalyseFunction
public class GetOnlinePlayers extends SimpleCommandAnalyse implements IAnalyse{

    @NotNull
    @Override
    public String getKeyword() {
        return "查询在线玩家";
    }

    @NotNull
    @Override
    public IAction getAction() {
        return (rpcDataPack) -> {
            StringBuilder onlinePlayers = new StringBuilder("在线玩家 "+ Bukkit.getOnlinePlayers().size()+" 人\n");
            for (Player p : Bukkit.getOnlinePlayers())
            {
                onlinePlayers.append(p.getName());
                onlinePlayers.append(" ");
            }
            MQService.INSTANCE.sendCommandFeedBack(rpcDataPack,new SimpleCommandResultBody(true,onlinePlayers.toString()));
//            RPCDataPack datapack = new RPCDataPack.Builder()
//                    .setDatapackBody()
//                    .setType(DataPackType.COMMAND_RESULT)
//                    .addRoutingData(new RoutingData(ExchangeType.SINGLE_EXCHANGE, List.of(rpcDataPack.getSender().getQueueName())))
//                    .setUUID(rpcDataPack.getUuid())
//                    .build();
//            MQService.INSTANCE.sendDataPack(datapack);
        };
    }
}
