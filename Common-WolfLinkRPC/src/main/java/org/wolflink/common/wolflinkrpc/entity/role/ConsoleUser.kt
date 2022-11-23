package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType

// 本地控制台用户，一般在向远程发起指令、消息时使用
class ConsoleUser(queueName : String = RPCCore.configuration.getQueueName(), platform : ClientType = RPCCore.configuration.getClientType()) : RPCUser(queueName,platform,queueName,queueName)