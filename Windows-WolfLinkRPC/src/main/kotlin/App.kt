package org.wolflink.windows.wolflinkrpc

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.role.ConsoleSender
import org.wolflink.common.wolflinkrpc.service.RPCService
import java.util.Scanner

object App
{
    fun enable()
    {
        PersistenceCfg.loadCfg()
        // 为windows唯一用户设置权限
        PersistenceCfg.permissionMap.put(PersistenceCfg.queueName,PermissionLevel.ONLY_MANAGER);

        RPCCore.initSystem(RPCConfiguration)

        initScanner()
        //下方无法触及
    }
    fun disable()
    {
        PersistenceCfg.saveCfg()
        RPCCore.closeSystem()
    }

    private fun initScanner()
    {
        RPCLogger.info("Input scanner has been initialized.")
        val scanner = Scanner(System.`in`,"GBK")
        var inputString = scanner.nextLine()
        while (inputString != "stop")
        {
            RPCService.analyseCommand(ConsoleSender(RPCConfiguration.getQueueName(),RPCConfiguration.getClientType()),inputString)
            inputString = scanner.nextLine()
        }
        disable()
    }
}