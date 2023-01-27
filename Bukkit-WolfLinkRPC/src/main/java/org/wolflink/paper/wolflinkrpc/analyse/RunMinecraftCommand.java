package org.wolflink.paper.wolflinkrpc.analyse;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.RemoteCallHandler;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody;
import org.wolflink.common.wolflinkrpc.service.MQService;
import org.wolflink.paper.wolflinkrpc.App;

@RemoteCallHandler
public class RunMinecraftCommand extends SimpleRemoteHandler {

    @NotNull
    @Override
    public PermissionLevel getPermission() { return PermissionLevel.OWNER; }
    @NotNull
    @Override
    public String getKeyword() {
        return "bukkit指令";
    }
    @NotNull
    @Override
    public IAction getAction() {
        return datapack -> {
            String originCommand = datapack.getJsonObject().get("command").getAsString();
            String command = getCommand(originCommand);
            App.RPC_LOGGER.info("收到远程指令，原始内容: "+originCommand+" 解析内容: "+command);
            if (command.length() == 0) {
                App.RPC_LOGGER.info("指令体为空");
                MQService.INSTANCE.sendCommandFeedBack(datapack,
                        new SimpleCommandResultBody(false,"指令格式错误，请参考以下格式: bukkit指令 kill MikkoAyaka"));
                return;
            }
                Bukkit.getScheduler().runTask(App.INSTANCE,() -> {
                    try {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        MQService.INSTANCE.sendCommandFeedBack(datapack,
                                new SimpleCommandResultBody(true,"指令执行成功。"));
                        App.RPC_LOGGER.info("指令执行成功，已返回结果。");
                    } catch (CommandException e) {
                        MQService.INSTANCE.sendCommandFeedBack(datapack,
                                new SimpleCommandResultBody(false,"指令在服务器内执行时遇到了问题，以下是报错日志: "+e.getMessage()));
                        App.RPC_LOGGER.info("指令执行失败，已返回结果。");
                    }
                });
        };
    }
}
