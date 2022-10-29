package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;

@AnalyseFunction
public class DisableServerPlugin extends SimpleCommandAnalyse {

    @NotNull
    @Override
    public PermissionLevel getPermission(){
        return PermissionLevel.ADMIN;
    }

    @NotNull
    @Override
    public String getKeyword() {
        return "禁用插件";
    }

    @NotNull
    @Override
    public IAction getAction() {
        return datapack -> {
            String originCommand = datapack.getJsonObject().get("command").getAsString();
            String pluginName = getCommand(originCommand);
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if(plugin == null)
            {
                MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"未能根据名称找到对应的插件，插件名称："+pluginName));
                return;
            }
            if(!plugin.isEnabled())
            {
                MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"插件已经被禁佣了，请勿重复操作"));
                return;
            }
            Bukkit.getPluginManager().disablePlugin(plugin);
            MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"插件禁用成功，但下次重启后仍会生效"));
        };
    }
}
