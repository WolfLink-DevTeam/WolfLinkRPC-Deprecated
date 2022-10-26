package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender

open class SimpleSender(private var queueName : String,private var senderName : String, private var uniqueID : String, private var platform : ClientType) :
    ISender {
    override fun getQueueName(): String = queueName
    override fun getSenderName(): String = senderName

    override fun getUniqueID(): String = uniqueID

    override fun getPlatform(): ClientType = platform
}