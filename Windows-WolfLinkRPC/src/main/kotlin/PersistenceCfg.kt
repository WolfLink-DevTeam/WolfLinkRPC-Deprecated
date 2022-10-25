package org.wolflink.windows.wolflinkrpc

import org.wolflink.windows.wolflinkrpc.entity.MCServer
import org.wolflink.windows.wolflinkrpc.entity.MCServerDataClass
import java.io.File
import java.nio.charset.StandardCharsets

object PersistenceCfg {

    private var map : MutableMap<String,Any> = mutableMapOf()

    var mcServerDataMap : MutableMap<String,MCServer> = mutableMapOf()

    private const val mainConfigFileName = "config.yml"
    private const val serverDataFileName = "ServerData.yml"

    fun getMCServerData(serverName : String) : MCServer?
    {
        return mcServerDataMap[serverName]
    }

    fun addMCServerData(mcServer : MCServer)
    {
        mcServerDataMap[mcServer.data.name] = mcServer
    }

    fun <T> getValue(key : String,default : T) : T
    {
        val t : T
        return try{
            t = map[key] as T
            t ?: default
        } catch (e : ClassCastException) {
            default
        }
    }
    fun loadCfg()
    {
        loadMainCfg()
        loadMCServerData()
    }
    fun saveCfg()
    {
        saveMainCfg()
        saveMCServerData()
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

    private fun loadMainCfg()
    {
        val file = File(System.getProperty("user.dir"), mainConfigFileName)
        if(!file.exists())
        {
            file.createNewFile()
            file.writeText("""
                测试配置项1 = "abab"
                测试配置项2 = "abab"
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