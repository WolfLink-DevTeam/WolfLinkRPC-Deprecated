package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

interface IPredicate {
    fun invoke(datapack : RPCDataPack) : Boolean
}