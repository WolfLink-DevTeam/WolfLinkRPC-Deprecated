package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody

class SimpleCommandResultBody(private var sender: ISender, private var result: Boolean, private var info: String) :ICommandResultBody {
    override fun getSender(): ISender = sender

    override fun getResult(): Boolean = result

    override fun getInfo(): String = info
}