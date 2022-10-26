package org.wolflink.common.wolflinkrpc.api.interfaces.command

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender

interface ICommandFunction {
    fun getCommand() : String
    fun invoke(sender : ISender, args : List<String> = listOf()) : Boolean
    fun getPermission() : PermissionLevel = PermissionLevel.DEFAULT
}