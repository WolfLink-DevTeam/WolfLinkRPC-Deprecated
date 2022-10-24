package org.wolflink.common.wolflinkrpc.service

import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

object RPCService {

    // 判断谓词 执行函数 映射表
    lateinit var analyseFunctionList : MutableList<IAnalyse>

    fun analyseDatapack(datapack : RPCDataPack)
    {
        for (analyseFunction in analyseFunctionList) if(analyseFunction.getPredicate().invoke(datapack))analyseFunction.getAction().invoke(datapack)
    }

}