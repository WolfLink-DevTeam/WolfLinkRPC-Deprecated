package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender

open class SimpleSender(private var name : String, private var uniqueID : String, private var platform : ClientType) :
    ISender {
    override fun getName(): String = name

    override fun getUniqueID(): String = uniqueID

    override fun getPlatform(): ClientType = platform
}