package org.wolflink.paper.wolflinkrpc.analyse;

import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction;
import org.wolflink.common.wolflinkrpc.entity.impl.RemoteCommandHelpImpl;

@AnalyseFunction
public class RemoteCommandHelp extends RemoteCommandHelpImpl {

    public RemoteCommandHelp() {
        super("查询在线玩家 - 返回当前服务器内所有在线玩家的ID\n" +
                "bukkit指令 {指令内容} - 以控制台的身份执行bukkit指令，例如: kill MikkoAyaka\n" +
                "关闭服务器 - 在10秒后自动关闭服务器");
    }
}
