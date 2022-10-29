package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.entity.RoutingData

// 广播接收者 会将你的数据包广播到当前在线的所有RPC客户端，包括发起方自身。
class BroadcastReceiver : RPCUser("",ClientType.UNKNOWN,"","", RoutingData(ExchangeType.ALL_EXCHANGE, mutableListOf("broadcast.all"))) {
}