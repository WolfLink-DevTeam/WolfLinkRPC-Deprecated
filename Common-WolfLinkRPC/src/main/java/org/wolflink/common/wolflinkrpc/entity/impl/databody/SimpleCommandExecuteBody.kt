package org.wolflink.common.wolflinkrpc.entity.impl.databody

import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandExecuteBody

class SimpleCommandExecuteBody(private var command : String) : ICommandExecuteBody {

    override fun getCommand(): String = command
}