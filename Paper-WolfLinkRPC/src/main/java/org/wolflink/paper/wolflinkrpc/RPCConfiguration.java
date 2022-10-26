package org.wolflink.paper.wolflinkrpc;

import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.enums.ClientType;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration;
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger;
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse;

import java.util.*;

public class RPCConfiguration implements IConfiguration {

    public Boolean getDebug(){
        return App.INSTANCE.getConfig().getBoolean("Debug",false);
    }

    @NotNull
    @Override
    public String getHost() {
        return App.INSTANCE.getConfig().getString("Host","");
    }

    @Override
    public int getPort() {
        return App.INSTANCE.getConfig().getInt("Port",10000);
    }

    @NotNull
    @Override
    public String getUsername() {
        return App.INSTANCE.getConfig().getString("UserName","");
    }

    @NotNull
    @Override
    public String getPassword() {
        return App.INSTANCE.getConfig().getString("Password","");
    }

    @NotNull
    @Override
    public String getQueueName() {
        return App.INSTANCE.getConfig().getString("QueueName","");
    }

    @NotNull
    @Override
    public ILogger getLogger() {
        return App.RPC_LOGGER;
    }

    @NotNull
    @Override
    public ClientType getClientType() {
        return ClientType.BUKKIT;
    }

    public String getProjectChineseName(boolean isShort){
        if(isShort)return "§7[ §b绫狼§9RPC §7]";
        else return "§7[ §b绫狼互联§8-§9RPC §7]";
    }
    public String getProjectEnglishName() { return  "§7[ §bWolfLink§9RPC §7]"; }

    @NotNull
    @Override
    public String getAnalyseFunctionPackage() {
        return "org.wolflink.paper.wolflinkrpc.analyse";
    }

    @NotNull
    @Override
    public Class<?> getMainClass() {
        return App.class;
    }

    @NotNull
    @Override
    public List<IAnalyse> getAnalyseFunctionList() {
        return DefaultImpls.getAnalyseFunctionList(this);
    }

    @NotNull
    @Override
    public String getCommandFunctionPackage() {
        return "org.wolflink.paper.wolflinkrpc.command";
    }

    @NotNull
    @Override
    public Map<String, PermissionLevel> getPermissionGroupMap()
    {
        //TODO 把这个配置放到配置文件当中
        Map<String,PermissionLevel> map = new HashMap<>();
        map.put("3401286177",PermissionLevel.ONLY_MANAGER);
        return map;
    }
}
