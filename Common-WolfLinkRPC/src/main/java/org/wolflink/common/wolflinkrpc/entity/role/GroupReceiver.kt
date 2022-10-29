package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.entity.RoutingData

// 小组接收者对象，例如mirai小组、bukkit小组，使用该类型的接收者会将你的数据包广播到指定小组
class GroupReceiver(val groupName : String) : RPCUser("",
    ClientType.UNKNOWN,"","", RoutingData(ExchangeType.GROUP_EXCHANGE, mutableListOf("broadcast.$groupName"))
)