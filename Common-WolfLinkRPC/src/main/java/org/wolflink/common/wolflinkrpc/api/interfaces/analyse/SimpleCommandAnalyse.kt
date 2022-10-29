package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.enums.reach
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.service.PermissionService

//简单指令，数据包类型为COMMAND_EXECUTE，并且判断是否以关键词开头，提供getCommand方法，截取关键词之后的部分
abstract class SimpleCommandAnalyse : IAnalyse {

    abstract fun getKeyword() : String
    open fun getPermission() : PermissionLevel = PermissionLevel.DEFAULT

    fun getCommand(str: String): String {
        var keywordSize = getKeyword().length
        keywordSize++
        if (str.length <= keywordSize) return ""
        return str.substring(keywordSize)
    }

    override fun getPredicate(): IPredicate {

        return object : IPredicate{
            override fun invoke(datapack: RPCDataPack): Boolean {
                return datapack.type == DataPackType.COMMAND_EXECUTE
                        && datapack.jsonObject.get("command").asString.startsWith(getKeyword())
                        && PermissionService.getUserPermission(datapack.sender.uniqueID) reach getPermission()
            }
        }
    }

    abstract override fun getAction(): IAction
}