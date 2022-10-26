package org.wolflink.windows.wolflinkrpc.command

import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.entity.impl.command.SendAllTextMessageImpl

@CommandFunction
class SendAllTextMessage : SendAllTextMessageImpl(){
    override fun getPermission(): PermissionLevel = PermissionLevel.DEFAULT
}