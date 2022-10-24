package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

interface IAction {
    fun invoke(datapack : RPCDataPack)
}