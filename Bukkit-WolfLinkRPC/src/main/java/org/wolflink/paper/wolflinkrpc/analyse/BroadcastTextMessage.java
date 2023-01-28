package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler;
import org.wolflink.common.wolflinkrpc.api.enums.ClientType;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IRemoteHandler;
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser;
import org.wolflink.paper.wolflinkrpc.App;

@RemoteCallHandler
public class BroadcastTextMessage implements IRemoteHandler {
    @NotNull
    @Override
    public IPredicate getPredicate() {
        return (rpcDataPack) -> rpcDataPack.getType() == DataPackType.TEXT_MESSAGE;
    }

    @NotNull
    @Override
    public IAction getAction() {
        return (rpcDataPack) -> {
            RPCUser sender = rpcDataPack.getSender();
            if(sender.getClientType() != ClientType.ANONYMOUS) Bukkit.broadcastMessage(App.RPC_CONFIGURATION.getProjectChineseName(true)+" §a"+sender.getUserName()+" §8» §f"+rpcDataPack.getJsonObject().get("msg").getAsString());
            else Bukkit.broadcastMessage(rpcDataPack.getJsonObject().get("msg").getAsString());
        };
    }
}
