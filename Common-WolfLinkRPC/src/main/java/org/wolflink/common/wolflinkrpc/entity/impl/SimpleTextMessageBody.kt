package org.wolflink.common.wolflinkrpc.entity.impl

import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody

class SimpleTextMessageBody(private var sender : ISender, private var msg : String) : ITextMessageBody {
    override fun getSender(): ISender = sender
    override fun getMsg(): String = msg
}