package org.wolflink.windows.wolflinkrpc.command

import org.wolflink.common.wolflinkrpc.api.annotations.LocalCallHandler
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.impl.handler.local.SendGroupTextMessageImpl

@LocalCallHandler
class SendGroupTextMessage : SendGroupTextMessageImpl(){
    override fun getPermission(): PermissionLevel = PermissionLevel.DEFAULT
}