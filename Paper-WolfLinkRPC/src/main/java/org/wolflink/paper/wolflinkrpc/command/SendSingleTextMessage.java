package org.wolflink.paper.wolflinkrpc.command;

import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType;
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender;
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;
import org.wolflink.common.wolflinkrpc.entity.RoutingData;
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleTextMessageBody;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;
import org.wolflink.paper.wolflinkrpc.utils.StringUtil;

import java.util.List;

@CommandFunction
public class SendSingleTextMessage implements ICommandFunction {
    @NotNull
    @Override
    public String getCommand() {
        return "> 互通消息";
    }
    @Override
    public boolean invoke(@NotNull ISender sender, @NotNull List<String> args) {
        if(args.size() < 2)return false;
        String routingKey = args.get(0);
        String message = StringUtil.joinToString(args.subList(1,args.size())," ");
        RPCDataPack datapack = new RPCDataPack.Builder()
                .setDatapackBody(new SimpleTextMessageBody(sender,message))
                .addRoutingData(new RoutingData(ExchangeType.SINGLE_EXCHANGE, List.of(routingKey)))
                .setType(DataPackType.TEXT_MESSAGE)
                .setSenderName(App.RPC_CONFIGURATION.getQueueName())
                .build();
        MQService.INSTANCE.sendDataPack(datapack);
        return true;
    }
}
