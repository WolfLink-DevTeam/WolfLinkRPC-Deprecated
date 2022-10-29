package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.service.PermissionService
// 最基础的RPCUser类
open class RPCUser(val queueName : String,
                   val clientType : ClientType,
                   val userName : String,
                   val uniqueID : String,
                   val routingData: RoutingData = RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(queueName)))
{
    fun getPermission() : PermissionLevel = PermissionService.getUserPermission(uniqueID)
}