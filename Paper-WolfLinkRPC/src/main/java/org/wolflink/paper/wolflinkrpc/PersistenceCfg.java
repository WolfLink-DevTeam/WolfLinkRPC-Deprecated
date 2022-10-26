package org.wolflink.paper.wolflinkrpc;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PersistenceCfg {
    private static PersistenceCfg INSTANCE;

    private String host = "127.0.0.1";
    private int port = 0;
    private String username = "username";
    private String password = "password";
    private String queueName = "windows_default";
    private boolean debug = false;
    private Map<String, PermissionLevel> permissionMap = new HashMap<>();

    private FileConfiguration mainConfig;
    private PersistenceCfg(){
        App.INSTANCE.saveDefaultConfig();
        loadCfg();
    }
    public static PersistenceCfg getInstance()
    {
        if(INSTANCE == null)INSTANCE = new PersistenceCfg();
        return INSTANCE;
    }
    public void loadCfg(){
        loadMainConfig();
    }
    public void saveCfg() {
        saveMainConfig();
    }
    private void loadMainConfig(){
        mainConfig = App.INSTANCE.getConfig();
        host = mainConfig.getString("Host",host);
        port = mainConfig.getInt("Port",port);
        username = mainConfig.getString("Username",username);
        password = mainConfig.getString("Password",password);
        queueName = "bukkit_"+mainConfig.getString("ClientTag","paper_default");
        debug = mainConfig.getBoolean("Debug");
        List<String> permissionList = mainConfig.getStringList("PermissionList");
        for (String str : permissionList)
        {
            String[] args = str.split(" \\| ");
            if(args.length != 2)continue;
            PermissionLevel level;
            try{
                level = PermissionLevel.valueOf(args[1]);
            } catch (IllegalArgumentException e)
            {
                continue;
            }
            permissionMap.put(args[0],level);
        }
    }
    private void saveMainConfig(){
        List<String> permissionList = new ArrayList<>();
        for (Map.Entry<String,PermissionLevel> entry : permissionMap.entrySet())
        {
            permissionList.add(entry.getKey()+" | "+entry.getValue().name().toUpperCase());
        }
        mainConfig.set("PermissionList",permissionList);
        App.INSTANCE.saveConfig();
    }
}
