package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

// 执行函数
interface IAction {
    fun invoke(datapack : RPCDataPack)
}