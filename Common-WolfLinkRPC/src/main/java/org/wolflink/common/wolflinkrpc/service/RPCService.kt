package org.wolflink.common.wolflinkrpc.service

import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.entity.CommandData
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

object RPCService {

    // 判断谓词 执行函数 映射表
    lateinit var analyseFunctionList : MutableList<IAnalyse>

    //解析数据包，根据谓词进行判断，谓词判断通过执行相应方法
    fun analyseDatapack(datapack : RPCDataPack)
    {
        for (analyseFunction in analyseFunctionList) if(analyseFunction.getPredicate().invoke(datapack))analyseFunction.getAction().invoke(datapack)
    }
    //将字符串视为命令进行解析，返回执行结果
    fun analyseCommand(sender: ISender, command : String) : Boolean = CommandData.runCommand(sender,command)

}