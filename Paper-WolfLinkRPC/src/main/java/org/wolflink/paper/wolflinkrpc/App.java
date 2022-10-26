package org.wolflink.paper.wolflinkrpc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.wolflink.common.wolflinkrpc.RPCCore;
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger;
import org.wolflink.paper.wolflinkrpc.listener.OnPlayerChat;
import org.wolflink.paper.wolflinkrpc.listener.OnServerClose;

public final class App extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    public static RPCConfiguration RPC_CONFIGURATION;
    public static ILogger RPC_LOGGER;
    @Override
    public void onEnable() {
        INSTANCE = this;

        PersistenceCfg.getInstance();

        RPC_CONFIGURATION = new RPCConfiguration();
        RPC_LOGGER = new RPCLogger();
        this.saveDefaultConfig();
        // Plugin startup logic
        RPCCore.INSTANCE.initSystem(RPC_CONFIGURATION);

        initListener();
    }
    public void initListener()
    {
        Bukkit.getPluginManager().registerEvents(new OnPlayerChat(),this);
        Bukkit.getPluginManager().registerEvents(new OnServerClose(),this);
    }

    @Override
    public void onDisable() {
        PersistenceCfg.getInstance().saveCfg();
        // Plugin shutdown logic
    }
}
