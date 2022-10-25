package org.wolflink.paper.wolflinkrpc.analyse;

import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate;
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleSender;
import org.wolflink.paper.wolflinkrpc.App;

@AnalyseFunction
public class BroadcastTextMessage implements IAnalyse {
    @NotNull
    @Override
    public IPredicate getPredicate() {
        return (rpcDataPack) -> rpcDataPack.getType() == DataPackType.TEXT_MESSAGE;
    }

    @NotNull
    @Override
    public IAction getAction() {
        return (rpcDataPack) -> {
            ISender sender = new Gson().fromJson(rpcDataPack.getJsonObject().get("sender").getAsString(), SimpleSender.class);
            Bukkit.broadcast(Component.text(App.RPC_CONFIGURATION.getProjectChineseName(true)+" §a"+sender.getName()+" §8» §f"+rpcDataPack.getJsonObject().get("msg").getAsString()));
        };
    }
}
