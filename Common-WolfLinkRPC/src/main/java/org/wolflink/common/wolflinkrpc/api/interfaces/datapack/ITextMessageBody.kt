package org.wolflink.common.wolflinkrpc.api.interfaces.datapack

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender

interface ITextMessageBody : IDataPackBody {
    fun getSender() : ISender
    fun getMsg() : String
    override fun toJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("sender",Gson().toJson(getSender()))
        jsonObject.addProperty("msg",getMsg())
        return jsonObject
    }
}