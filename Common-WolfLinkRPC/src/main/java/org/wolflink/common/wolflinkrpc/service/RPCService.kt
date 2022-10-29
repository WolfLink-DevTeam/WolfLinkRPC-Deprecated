package org.wolflink.common.wolflinkrpc.service

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser

object RPCService {

    // 判断谓词 执行函数 映射表
    lateinit var analyseFunctionList : MutableList<IAnalyse>


    //初始化数据包处理服务
    fun init(configuration: IConfiguration)
    {
        analyseFunctionList = configuration.getAnalyseFunctionList()
    }
    //解析数据包，根据谓词进行判断，谓词判断通过执行相应方法
    fun analyseDatapack(datapack : RPCDataPack)
    {
        var exist = 0
        for (analyseFunction in analyseFunctionList)
        {
            if(analyseFunction.getPredicate().invoke(datapack))
            {
                analyseFunction.getAction().invoke(datapack)
                exist++
            }
        }
        if(exist == 0 && datapack.type != DataPackType.COMMAND_RESULT)
        {
            MQService.sendCommandFeedBack(datapack, SimpleCommandResultBody(false,"远程指令未能得到运行，可能的原因：权限不足、关键词不匹配。\n如需提权，请为用户 ${datapack.sender.uniqueID} 设置权限。"))
        }
        else
        {
            RPCCore.logger.info("Remote command has been matched for $exist times .")
        }
    }
    //将字符串视为命令进行解析，返回执行结果
    fun analyseCommand(sender: RPCUser, command : String) : Pair<Boolean,String> = CommandService.runCommand(sender,command)

}