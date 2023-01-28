package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;

@RemoteCallHandler
public class ServerClose extends SimpleRemoteHandler {

    @NotNull
    @Override
    public PermissionLevel getPermission(){ return PermissionLevel.OWNER; }

    @NotNull
    @Override
    public String getKeyword() {
        return "关闭服务器";
    }
    @NotNull
    @Override
    public IAction getAction() {
        return (rpcDataPack) -> {
            Bukkit.broadcastMessage(App.RPC_CONFIGURATION.getProjectChineseName(false)+" 服务器即将在10秒后关闭。");
            for (Player p : Bukkit.getOnlinePlayers())
            {
                p.sendTitle("§7[ §e! §7] §f服务器即将在 §c10秒 §f后关闭","§7请做好准备",20,60,20);
                p.playSound(p.getLocation(),Sound.ENTITY_ENDER_DRAGON_GROWL,1,1.5f);
            }
            Bukkit.getScheduler().runTaskLater(App.INSTANCE,() -> {
                App.INSTANCE.getServer().dispatchCommand(Bukkit.getConsoleSender(),"stop");
            },20 * 10L);
            MQService.INSTANCE.sendCommandFeedBack(rpcDataPack,
                    new SimpleCommandResultBody(true,"指令执行成功"));
        };
    }
}
