package org.wolflink.common.wolflinkrpc.api.interfaces.datapack

import com.google.gson.JsonObject

interface ITextMessageBody : IDataPackBody {
    fun getMsg() : String
    override fun toJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("msg",getMsg())
        return jsonObject
    }
}