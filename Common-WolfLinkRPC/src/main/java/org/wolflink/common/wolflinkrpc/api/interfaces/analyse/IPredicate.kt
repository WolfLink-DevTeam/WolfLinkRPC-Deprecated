package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

// 条件谓词
interface IPredicate {
    fun invoke(datapack : RPCDataPack) : Boolean
}