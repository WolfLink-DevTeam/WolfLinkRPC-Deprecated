package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.entity.RoutingData
// RPC客户端接收者，发送指令等操作一般会用到
class ClientReceiver(queueName : String) : RPCUser(queueName,ClientType.UNKNOWN,"","",
    RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf(queueName)))