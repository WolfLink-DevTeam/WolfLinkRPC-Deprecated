package org.wolflink.windows.wolflinkrpc

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.windows.wolflinkrpc.entity.MCServer
import org.wolflink.windows.wolflinkrpc.entity.MCServerDataClass
import java.io.File
import java.nio.charset.StandardCharsets

object PersistenceCfg {

    private var map : MutableMap<String,Any> = mutableMapOf()

    var mcServerDataMap : MutableMap<String,MCServer> = mutableMapOf()

    private const val mainConfigFileName = "config.txt"
    //服务器数据可读写
    private const val serverDataFileName = "ServerData.txt"

    private const val permissionFileName = "PermissionData.txt"

    var host : String = "127.0.0.1"
    var port : Int = 0
    var username : String = "username"
    var password : String = "password"
    var queueName : String = "windows_default"
    var debug : Boolean = false
    val permissionMap : MutableMap<String,PermissionLevel> = mutableMapOf()

    fun getMCServerData(serverName : String) : MCServer?
    {
        return mcServerDataMap[serverName]
    }

    fun addMCServerData(mcServer : MCServer)
    {
        mcServerDataMap[mcServer.data.name] = mcServer
    }

    fun getString(key : String) = map[key] as String
    fun getBoolean(key: String) = getString(key).toBoolean()
    fun getInt(key: String) = getString(key).toInt()
    fun getDouble(key: String) = getString(key).toDouble()


    fun loadCfg()
    {
        loadMainCfg()
        loadMCServerData()
        loadPermissionCfg()
    }
    fun saveCfg()
    {
        saveMainCfg()
        saveMCServerData()
        savePermissionCfg()
    }

    private fun loadMCServerData()
    {
        val file = File(System.getProperty("user.dir"), serverDataFileName)
        if(!file.exists())
        {
            file.createNewFile()
            RPCLogger.info("No minecraft server data instance has been loaded , please add it from other clients.")
            return
        }else{ //存在ServerData.yml文件，尝试从中读取数据
            val dataString = String(file.inputStream().readBytes(),StandardCharsets.UTF_8)
            val dataArray = dataString.split("\n")
            for (data in dataArray)
            {
                if(data.isEmpty())continue
                val serverData = MCServer(MCServerDataClass.fromJson(data,MCServerDataClass::class.java))
                mcServerDataMap[serverData.data.name] = serverData
            }
            RPCLogger.info("${mcServerDataMap.size} minecraft server data instances have been loaded.")
        }
        RPCLogger.info("ServerData has been loaded.")
    }
    private fun loadPermissionCfg()
    {
        val file = File(System.getProperty("user.dir"), permissionFileName)
        if(!file.exists())
        {
            file.createNewFile()
            RPCLogger.info("No permission data has been loaded , create a new file .")
        }else
        {
            val dataString = String(file.inputStream().readBytes(),StandardCharsets.UTF_8)
            val dataArray = dataString.split("\n")
            for (data in dataArray)
            {
                if(data.isEmpty())continue
                val (key,value) = data.split(" | ")
                permissionMap[key] = PermissionLevel.valueOf(value.uppercase())
            }
            RPCLogger.info("PermissionData has been loaded.")
        }
    }
    private fun savePermissionCfg()
    {
        val file = File(System.getProperty("user.dir"), permissionFileName)
        if(!file.exists())
        {
            file.createNewFile()
        }
        var text = ""
        for ((key,value) in permissionMap)
        {
            text += "$key | ${value.name.uppercase()}\n"
        }
        file.setWritable(true)
        file.writeText(text)
        RPCLogger.info("PermissionData has been saved.")
    }

    private fun loadMainCfg()
    {
        val file = File(System.getProperty("user.dir"), mainConfigFileName)
        if(!file.exists())
        {
            file.createNewFile()
            file.writeText("""
                Host = 127.0.0.1
                Port = 10000
                Username = username
                Password = password
                ClientTag = default
                Debug = false
            """.trimIndent())
        }
        val str = String(file.inputStream().readBytes(),StandardCharsets.UTF_8)
        val strArray = str.split("\n")
        for (_str in strArray)
        {
            if(_str.split(" = ").size != 2) continue
            val (key,value) = _str.split(" = ")
            map[key] = value
        }
            host = getString("Host")
            port = getInt("Port")
            username = getString("Username")
            password = getString("Password")
            queueName = "windows_${getString("ClientTag")}"
            debug = getBoolean("Debug")
        RPCLogger.info("Config has been loaded.")
    }
    private fun saveMainCfg()
    {
        val file = File(System.getProperty("user.dir"), mainConfigFileName)
        if(!file.exists())
        {
            file.createNewFile()
        }
        var text = ""
        for ((key,value) in map)
        {
            text += "$key = $value\n"
        }
        file.setWritable(true)
        file.writeText(text)
        RPCLogger.info("Config has been saved.")
    }
    private fun saveMCServerData()
    {
        val file = File(System.getProperty("user.dir"), serverDataFileName)
        if(!file.exists())
        {
            file.createNewFile()
        }
        var text = ""
        for (mcServerData in mcServerDataMap.values)
        {
            text += MCServerDataClass.toJson(mcServerData.data)
            text += "\n"
        }
        file.setWritable(true)
        file.writeText(text)
        RPCLogger.info("ServerData has been saved.")
    }
}