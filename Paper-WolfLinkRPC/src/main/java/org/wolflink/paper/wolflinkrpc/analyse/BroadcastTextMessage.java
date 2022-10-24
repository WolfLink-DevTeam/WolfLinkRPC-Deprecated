package org.wolflink.paper.wolflinkrpc.analyse;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.BaseConfiguration;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate;

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
            Bukkit.broadcast(Component.text(BaseConfiguration.INSTANCE.getProjectChineseName()+" §f"+rpcDataPack.getJsonObject().get("msg").getAsString()));
        };
    }
}
