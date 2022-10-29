package org.wolflink.common.wolflinkrpc.entity.role

import org.wolflink.common.wolflinkrpc.api.enums.ClientType

// 匿名用户，用处不大，可以在客户端添加实现，隐藏匿名用户的发言前缀等
class AnonymousUser(queueName : String) : RPCUser(queueName,ClientType.ANONYMOUS,"","")