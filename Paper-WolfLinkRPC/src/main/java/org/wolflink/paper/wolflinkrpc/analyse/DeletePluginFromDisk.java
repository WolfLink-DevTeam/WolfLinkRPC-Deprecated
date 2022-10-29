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
import org.wolflink.paper.wolflinkrpc.App;

import java.io.File;

@AnalyseFunction
public class DeletePluginFromDisk extends SimpleCommandAnalyse {

    @NotNull
    @Override
    public PermissionLevel getPermission(){
        return PermissionLevel.ADMIN;
    }

    @NotNull
    @Override
    public String getKeyword() {
        return "删除插件";
    }

    @NotNull
    @Override
    public IAction getAction() {
        return datapack -> {
            String originCommand = datapack.getJsonObject().get("command").getAsString();
            String pluginFileName = getCommand(originCommand);
            String pluginFolder = "plugins";
            String pluginPath = pluginFolder+"\\"+pluginFileName;
            File file = new File(pluginPath);
            if(!file.exists())
            {
                MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"未能根据名称找到对应的插件文件，插件路径："+pluginPath));
                return;
            }
            try{
                if(file.delete())
                {
                    MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(true,"插件jar文件已从硬盘中删除"));
                }
                else{
                    MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"插件jar文件删除失败，可能还未从服务器中卸载或被其他程序使用中"));
                }
            }catch (SecurityException e)
            {
                MQService.INSTANCE.sendCommandFeedBack(datapack,new SimpleCommandResultBody(false,"插件jar文件删除失败，错误信息："+e.getMessage()));
            }

        };
    }
}
