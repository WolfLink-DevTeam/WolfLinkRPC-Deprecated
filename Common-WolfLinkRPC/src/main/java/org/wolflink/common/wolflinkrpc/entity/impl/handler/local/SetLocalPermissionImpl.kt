package org.wolflink.common.wolflinkrpc.entity.impl.handler.local

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.enums.reach
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ILocalHandler
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser
import org.wolflink.common.wolflinkrpc.service.PermissionService

open class SetLocalPermissionImpl : ILocalHandler {
    override fun getCommand(): String = "> 设置权限"

    override fun invoke(sender : RPCUser, args: List<String>): Pair<Boolean,String> {
        if(args.size < 2)return Pair(false,"Not enough arguments .")
        val uniqueID = args[0]
        val targetPermission : PermissionLevel
        try{
            targetPermission = PermissionLevel.valueOf(args[1].uppercase())
        }catch (e : IllegalArgumentException)
        {
            return Pair(false,"Illegal permission name : ${args[1].uppercase()}")
        }
        if(targetPermission reach sender.getPermission())return Pair(false,"Permission denied , you can't set this permission level for others .")
        PermissionService.setUserPermission(uniqueID,targetPermission)
        return Pair(true,"Command executed successfully .")
    }
}