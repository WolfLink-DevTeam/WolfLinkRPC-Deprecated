package org.wolflink.common.wolflinkrpc.entity.impl.databody

import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody

class SimpleTextMessageBody(private var msg : String) : ITextMessageBody {
    override fun getMsg(): String = msg
}