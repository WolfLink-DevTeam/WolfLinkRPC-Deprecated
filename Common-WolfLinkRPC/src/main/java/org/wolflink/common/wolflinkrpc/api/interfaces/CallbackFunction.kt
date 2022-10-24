package org.wolflink.common.wolflinkrpc.api.interfaces

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack

interface CallbackFunction {
    fun success(datapack : RPCDataPack)
    {
        RPCCore.logger.info("Datapack ${datapack.uuid} success to finish feedback.")
    }
    fun failed(datapack: RPCDataPack)
    {
        RPCCore.logger.warn("Datapack ${datapack.uuid} failed to finish feedback.")
    }
}