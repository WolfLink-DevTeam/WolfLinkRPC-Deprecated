package org.wolflink.common.wolflinkrpc.api.interfaces

import com.google.gson.Gson

/**
 * 支持对简单数据对象进行Json转换
 */
interface JsonSerializable<T> {

    fun toJson(t : T) : String = Gson().toJson(t)

    fun fromJson(jsonString: String,clazz : Class<T>) : T = Gson().fromJson(jsonString,clazz)

}