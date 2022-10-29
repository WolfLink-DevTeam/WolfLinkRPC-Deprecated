package org.wolflink.paper.wolflinkrpc.analyse;

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.entity.impl.analyse.RemoteCommandHelpImpl;

@AnalyseFunction
public class RemoteCommandHelp extends RemoteCommandHelpImpl {

    public RemoteCommandHelp() {
        super("帮助 - 查看远程指令帮助\n"+
                "设置权限 {用户uniqueID} {权限名} - 权限等级必须低于发起者\n"+
                "查询在线玩家 - 返回当前服务器内所有在线玩家的ID\n" +
                "bukkit指令 {指令内容} - 以控制台的身份执行bukkit指令，例如: kill MikkoAyaka\n" +
                "关闭服务器 - 在10秒后自动关闭服务器\n"+
                "安装插件 {插件文件名.jar}\n"+
                "删除插件 {插件文件名.jar}\n"+
                "启用插件 {插件名}\n"+
                "禁用插件 {插件名}\n"+
                "插件列表\n");
    }
}
