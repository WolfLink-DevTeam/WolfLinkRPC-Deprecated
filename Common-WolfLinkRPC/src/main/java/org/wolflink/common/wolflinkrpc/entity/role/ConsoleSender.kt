package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.entity.role.SimpleSender

class ConsoleSender(queueName : String,platform : ClientType) : SimpleSender(queueName,queueName,queueName,platform){
}