package org.wolflink.common.wolflinkrpc.entity

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser

class CommandData(val commandText : String,val permission : PermissionLevel = PermissionLevel.DEFAULT) {

    val nextCommandList : MutableSet<CommandData> = mutableSetOf()
    var action : (sender : RPCUser, args : List<String>) -> Pair<Boolean,String> = { _, _ -> Pair(false,"Unknown command")}
}