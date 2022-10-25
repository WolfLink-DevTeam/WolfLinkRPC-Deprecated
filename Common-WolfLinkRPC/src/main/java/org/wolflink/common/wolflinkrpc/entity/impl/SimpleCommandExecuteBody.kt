package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandExecuteBody

class SimpleCommandExecuteBody(private var sender : ISender, private var command : String) : ICommandExecuteBody {

    override fun getSender() : ISender = sender
    override fun getCommand(): String = command
}