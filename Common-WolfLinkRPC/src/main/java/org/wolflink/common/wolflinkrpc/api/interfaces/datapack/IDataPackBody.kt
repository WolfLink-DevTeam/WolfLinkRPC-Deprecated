package org.wolflink.common.wolflinkrpc.api.interfaces.datapack

import com.google.gson.JsonObject

interface IDataPackBody {
    fun toJsonObject() : JsonObject
}