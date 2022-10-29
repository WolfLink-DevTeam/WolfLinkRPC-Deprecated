package org.wolflink.windows.wolflinkrpc

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.role.ConsoleUser
import org.wolflink.common.wolflinkrpc.service.RPCService
import java.util.Scanner

object App
{
    // 程序启用
    fun enable()
    {
        // 加载本地持久化配置文件
        PersistenceCfg.loadCfg()
        // 为windows唯一用户设置权限
        PersistenceCfg.permissionMap.put(PersistenceCfg.queueName,PermissionLevel.ONLY_MANAGER);

        // 调用RPC核心部分的初始化方法
        RPCCore.initSystem(RPCConfiguration)
        // 初始化Windows客户端的指令输入功能
        initScanner()
        //下方无法触及，不要写代码了
    }
    // 程序停止
    fun disable()
    {
        // 将内存中的数据保存到本地进行持久化
        PersistenceCfg.saveCfg()
        // 调用RPC核心部分的关闭方法，完成资源回收(不调用也没事，只是其他客户端不知道你下线了，连接资源仍然会被自动回收)
        RPCCore.closeSystem()
    }

    private fun initScanner()
    {
        RPCLogger.info("Input scanner has been initialized.")
        val scanner = Scanner(System.`in`,"GBK")
        var inputString = scanner.nextLine()
        while (inputString != "stop")
        {
            val pair = RPCService.analyseCommand(ConsoleUser(RPCConfiguration.getQueueName(),RPCConfiguration.getClientType()),inputString)
            RPCLogger.info("Command status : ${pair.first} Info : ${pair.second}")
            inputString = scanner.nextLine()
        }
        disable()
    }
}