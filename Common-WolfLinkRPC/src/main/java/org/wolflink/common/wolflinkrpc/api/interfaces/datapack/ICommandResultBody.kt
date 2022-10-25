package org.wolflink.common.wolflinkrpc.api.interfaces.datapack

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender

interface ICommandResultBody : IDataPackBody {
    fun getSender() : ISender
    fun getResult() : Boolean
    fun getInfo() : String
    override fun toJsonObject(): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.addProperty("sender", Gson().toJson(getSender()))
        jsonObject.addProperty("result",getResult())
        jsonObject.addProperty("info",getInfo())

        return jsonObject
    }
}