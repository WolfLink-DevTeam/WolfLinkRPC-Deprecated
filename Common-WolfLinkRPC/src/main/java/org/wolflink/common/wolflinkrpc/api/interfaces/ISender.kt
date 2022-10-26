package org.wolflink.common.wolflinkrpc.api.interfaces

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.service.PermissionService

interface ISender {
    fun getQueueName() : String = RPCCore.configuration.getQueueName()
    fun getSenderName(): String
    fun getUniqueID(): String
    fun getPlatform(): ClientType
    fun getPermission(): PermissionLevel = PermissionService.getUserPermission(this.getUniqueID())
}