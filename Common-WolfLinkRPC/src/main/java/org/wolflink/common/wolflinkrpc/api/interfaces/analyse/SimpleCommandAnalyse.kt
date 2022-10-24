package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService.sendDataPack
import java.util.List

abstract class SimpleCommandAnalyse : IAnalyse {

    abstract fun getKeyword() : String

    fun getCommand(str: String): String {
        var keywordSize = getKeyword().length
        keywordSize++
        if (str.length <= keywordSize) return ""
        return str.substring(keywordSize)
    }

    override fun getPredicate(): IPredicate {

        return object : IPredicate{
            override fun invoke(datapack: RPCDataPack): Boolean {
                return datapack.type == DataPackType.COMMAND_EXECUTE && datapack.jsonObject.get("command").asString == getKeyword()
            }
        }
    }

    abstract override fun getAction(): IAction
}