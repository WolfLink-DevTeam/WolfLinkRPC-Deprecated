package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleCommandAnalyse;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;

import java.io.File;

@AnalyseFunction
public class LoadPluginFromDisk extends SimpleCommandAnalyse {
    @NotNull
    @Override
    public PermissionLevel getPermission(){
        return PermissionLevel.ADMIN;
    }

    @NotNull
    @Override
    public String getKeyword() {
        return "安装插件";
    }

    @NotNull
    @Override
    public IAction getAction() {
        return datapack -> {
            String originCommand = datapack.getJsonObject().get("command").getAsString();
            String pluginFileName = getCommand(originCommand);
            String pluginFolder = "plugins\\";
            String pluginPath = pluginFolder+"\\"+pluginFileName;
            try {
                App.RPC_LOGGER.info("try loading plugin ( "+pluginPath+" )");
               Plugin plugin = Bukkit.getPluginManager().loadPlugin(new File(pluginPath));
               if(plugin == null)throw new InvalidPluginException();
            } catch (UnknownDependencyException e) {
                MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"插件缺少必要的依赖！错误信息："+e.getMessage()));
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"不可用的插件，错误信息："+e.getMessage()));
            }
            MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(true,"插件已成功从硬盘中加载，如需立即使用请输入指令 启用插件"));
        };
    }
}
