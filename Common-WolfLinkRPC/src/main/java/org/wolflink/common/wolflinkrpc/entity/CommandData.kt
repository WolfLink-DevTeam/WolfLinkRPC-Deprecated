package org.wolflink.common.wolflinkrpc.entity

import org.jetbrains.annotations.TestOnly
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleSender

class CommandData(val commandText : String,val permission : PermissionLevel = PermissionLevel.DEFAULT) {

    val nextCommandList : MutableSet<CommandData> = mutableSetOf()
    var action : (sender : ISender, args : List<String>) -> Boolean = { _, _ ->
        RPCCore.logger.info("unknown command.")
        false}
}