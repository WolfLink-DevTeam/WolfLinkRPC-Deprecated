package org.wolflink.common.wolflinkrpc.entity.impl

import lombok.AllArgsConstructor
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody

class SimpleCommandResultBody(@JvmField var result: Boolean,@JvmField var info: String) :ICommandResultBody {

    override fun getResult(): Boolean = result

    override fun getInfo(): String = info
}