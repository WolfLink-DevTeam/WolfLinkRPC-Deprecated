package org.wolflink.common.wolflinkrpc.api.interfaces

import org.wolflink.common.wolflinkrpc.api.enums.ClientType

interface ISender {
    fun getName() : String
    fun getUniqueID() : String
    fun getPlatform() : ClientType
}