package org.wolflink.common.wolflinkrpc.api.interfaces.datapack

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender

interface ITextMessageBody : IDataPackBody {
    fun getMsg() : String
    override fun toJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("msg",getMsg())
        return jsonObject
    }
}