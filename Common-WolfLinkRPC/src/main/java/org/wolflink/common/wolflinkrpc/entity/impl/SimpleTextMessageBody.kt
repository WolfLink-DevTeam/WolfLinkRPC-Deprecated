package org.wolflink.common.wolflinkrpc.entity.impl

import lombok.AllArgsConstructor
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody

class SimpleTextMessageBody(@JvmField var msg : String) : ITextMessageBody {


    override fun getMsg(): String = msg
}