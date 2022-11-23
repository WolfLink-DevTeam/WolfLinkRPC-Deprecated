package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;
import org.wolflink.paper.wolflinkrpc.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RemoteCallHandler
public class GetServerPlugins extends SimpleRemoteHandler {

    @NotNull
    @Override
    public PermissionLevel getPermission(){
        return PermissionLevel.ADMIN;
    }

    @NotNull
    @Override
    public String getKeyword(){
        return "插件列表";
    }

    @NotNull
    @Override
    public IAction getAction() {
        return dataPack -> {

            List<String> pluginNames = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList());
            String resultMsg = "-\n"
                    +App.RPC_CONFIGURATION.getProjectChineseName(false)+" 插件列表("+pluginNames.size()+")"
                    +"\n-\n";
            resultMsg += StringUtil.joinToString(pluginNames," ");

            MQService.INSTANCE.sendCommandFeedBack(dataPack,new SimpleCommandResultBody(true,resultMsg));
        };
    }
}
