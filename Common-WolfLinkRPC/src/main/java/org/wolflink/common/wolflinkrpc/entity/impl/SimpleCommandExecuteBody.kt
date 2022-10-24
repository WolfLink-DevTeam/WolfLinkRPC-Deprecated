package org.wolflink.common.wolflinkrpc.entity.impl

import lombok.AllArgsConstructor
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandExecuteBody

class SimpleCommandExecuteBody(@JvmField var command : String) : ICommandExecuteBody {

    override fun getCommand(): String = command
}