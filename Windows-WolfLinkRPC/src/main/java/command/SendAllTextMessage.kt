package org.wolflink.windows.wolflinkrpc.command

import org.wolflink.common.wolflinkrpc.api.annotations.LocalCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.impl.handler.local.SendAllTextMessageImpl

@LocalCallHandler
class SendAllTextMessage : SendAllTextMessageImpl(){
    override fun getPermission(): PermissionLevel = PermissionLevel.DEFAULT
}