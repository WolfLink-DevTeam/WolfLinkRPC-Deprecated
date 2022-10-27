package org.wolflink.common.wolflinkrpc.service

import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration

object PermissionService {
    lateinit var permissionGroupMap: MutableMap<String,PermissionLevel>

    fun init(configuration: IConfiguration) {
        permissionGroupMap = configuration.getPermissionGroupMap()
    }
    fun getUserPermission(uniqueID : String) : PermissionLevel = permissionGroupMap[uniqueID] ?: PermissionLevel.DEFAULT
    fun setUserPermission(uniqueID: String,permissionLevel: PermissionLevel){
        permissionGroupMap[uniqueID] = permissionLevel
    }
}