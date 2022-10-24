package org.wolflink.common.wolflinkrpc.entity

import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType

data class RoutingData(var exchangeType: ExchangeType = ExchangeType.SINGLE_EXCHANGE, var routingKeyList : MutableList<String> = mutableListOf())
{
    fun addRoutingKey(routingKey : String) : RoutingData
    {
        routingKeyList.add(routingKey)
        return this
    }
}