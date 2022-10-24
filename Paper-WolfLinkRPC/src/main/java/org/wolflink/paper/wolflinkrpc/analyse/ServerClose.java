package org.wolflink.paper.wolflinkrpc.analyse;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IPredicate;
import org.wolflink.paper.wolflinkrpc.App;

@AnalyseFunction
public class ServerClose implements IAnalyse {
    @NotNull
    @Override
    public IPredicate getPredicate() {
        return (rpcDataPack) ->  rpcDataPack.getType() == DataPackType.COMMAND_EXECUTE &&
                rpcDataPack.getJsonObject().get("command").getAsString().startsWith("关闭服务器");
    }

    @NotNull
    @Override
    public IAction getAction() {
        return (rpcDataPack) -> {
            Bukkit.broadcast(Component.text(App.RPC_CONFIGURATION.getProjectChineseName()+" 服务器即将在10秒后关闭。"));
            Bukkit.getScheduler().runTaskLater(App.INSTANCE,() -> {
                App.INSTANCE.getServer().shutdown();
            },20 * 10L);
        };
    }
}
