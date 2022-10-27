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
        return PersistenceCfg.getInstance().isDebug();
    }

    @NotNull
    @Override
    public String getHost() {
        return PersistenceCfg.getInstance().getHost();
    }

    @Override
    public int getPort() {
        return PersistenceCfg.getInstance().getPort();
    }

    @NotNull
    @Override
    public String getUsername() {
        return PersistenceCfg.getInstance().getUsername();
    }

    @NotNull
    @Override
    public String getPassword() {
        return PersistenceCfg.getInstance().getPassword();
    }

    @NotNull
    @Override
    public String getQueueName() {
        return PersistenceCfg.getInstance().getQueueName();
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
        return PersistenceCfg.getInstance().getPermissionMap();
    }
}
