package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.api.enums.ClientType

class ConsoleSender(queueName : String,platform : ClientType) : SimpleSender(queueName,queueName,queueName,platform){
}