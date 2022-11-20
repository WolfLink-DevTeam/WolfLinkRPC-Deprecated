package org.wolflink.common.wolflinkrpc.api.interfaces.command

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser

interface ILocalHandler {
    fun getCommand() : String
    fun invoke(sender : RPCUser, args : List<String> = listOf()) : Pair<Boolean,String>
    fun getPermission() : PermissionLevel = PermissionLevel.DEFAULT
}