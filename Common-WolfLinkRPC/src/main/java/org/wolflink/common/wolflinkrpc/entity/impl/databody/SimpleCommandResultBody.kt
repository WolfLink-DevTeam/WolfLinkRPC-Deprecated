package org.wolflink.common.wolflinkrpc.entity.impl.databody

import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody

class SimpleCommandResultBody(private var result: Boolean, private var info: String) :ICommandResultBody {

    override fun getResult(): Boolean = result

    override fun getInfo(): String = info
}