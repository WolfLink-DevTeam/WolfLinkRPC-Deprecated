package org.wolflink.windows.wolflinkrpc.entity

import org.wolflink.common.wolflinkrpc.api.interfaces.JsonSerializable

data class MCServerDataClass(val name : String,val windowTitle : String,val folderPath : String,val batName :String)
{
    companion object : JsonSerializable<MCServerDataClass>
}
