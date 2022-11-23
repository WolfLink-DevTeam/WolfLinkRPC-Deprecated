package org.wolflink.common.wolflinkrpc.entity.impl.handler.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.*
import org.wolflink.common.wolflinkrpc.BaseConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAction
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.SimpleRemoteHandler
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandResultBody
import org.wolflink.common.wolflinkrpc.service.MQService
import org.wolflink.common.wolflinkrpc.service.WebService
import java.io.IOException

open class GithubAPIImpl : SimpleRemoteHandler() {
    override fun getKeyword(): String = "github"

    override fun getAction(): IAction {
        return object : IAction {
            override fun invoke(datapack: RPCDataPack) {
                val originCommand = datapack.jsonObject["command"].asString
                val command = getCommand(originCommand)
                val args = command.split(" ")
                if(args.size == 2)
                {
                    if(args[0].lowercase() == "search-repo")
                    {
                        val repoName = args[1]
                        val request = Request.Builder().url("https://api.github.com/search/repositories?q=$repoName").build()
                        val call = WebService.httpClient.newCall(request)
                        call.enqueue(object : Callback{
                            override fun onFailure(call: Call, e: IOException) {
                                failed(datapack)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val message = response.body?.string() ?: ""
                                if(message.isEmpty()) failed(datapack)
                                else{
                                    var result = ""
                                    val jsonElement = JsonParser.parseString(message).asJsonObject
                                    val jsonArray = jsonElement.getAsJsonArray("items")
                                    if(jsonArray.isJsonNull || jsonArray.isEmpty) failed(datapack)
                                    else
                                    {
                                        var count = 0
                                        for(jsonElem in jsonArray)
                                        {
                                            if(count > 5)
                                            {
                                                result += "\n...(最多展示 5 条)..."
                                                break
                                            }
                                            val jsonObject = jsonElem.asJsonObject
                                            result += """
                                                仓库 ${jsonObject["full_name"]} 星星 ${jsonObject["stargazers_count"]} 语言 ${jsonObject["language"]}
                                                链接 ${jsonObject["html_url"]}
                                                简介 ${jsonObject["description"]}
                                                -
                                                
                                            """.trimIndent()
                                            count++
                                        }
                                        success(datapack,result)
                                    }
                                }
                            }
                        })
                        return
                    }
                    else if(args[0].lowercase() == "search-user")
                    {
                        val userName = args[1]
                        val request = Request.Builder().url("https://api.github.com/search/users?q=$userName").build()
                        val call = WebService.httpClient.newCall(request)
                        call.enqueue(object : Callback{
                            override fun onFailure(call: Call, e: IOException) {
                                failed(datapack)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val message = response.body?.string() ?: ""
                                if(message.isEmpty()) failed(datapack)
                                else{
                                    var result = ""
                                    val jsonElement = JsonParser.parseString(message).asJsonObject
                                    val jsonArray = jsonElement.getAsJsonArray("items")
                                    if(jsonArray.isJsonNull || jsonArray.isEmpty) failed(datapack)
                                    else
                                    {
                                        var count = 0
                                        for(jsonElem in jsonArray)
                                        {
                                            if(count > 20)
                                            {
                                                result += "\n...(最多展示 20 条)..."
                                                break
                                            }
                                            val jsonObject = jsonElem.asJsonObject
                                            result += """
                                                用户 ${jsonObject["login"]} 主页 ${jsonObject["html_url"]}
                                                
                                            """.trimIndent()
                                            count++
                                        }
                                        success(datapack,result)
                                    }
                                }
                            }
                        })
                        return
                    }
                }
                if(args.size == 3)
                {
                    if(args[0].lowercase() == "commits")
                    {
                        val userName = args[1]
                        val repoName = args[2]
                        val request = Request.Builder().url("https://api.github.com/repos/$userName/$repoName/commits").build()
                        val call = WebService.httpClient.newCall(request)
                        call.enqueue(object : Callback{
                            override fun onFailure(call: Call, e: IOException) {
                                failed(datapack)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val message = response.body?.string() ?: ""
                                if(message.isEmpty()) failed(datapack)
                                else{
                                    var result = ""
                                    val jsonArray = JsonParser.parseString(message).asJsonArray
                                    if(jsonArray.isJsonNull || jsonArray.isEmpty) failed(datapack)
                                    else
                                    {
                                        var count = 0
                                        for(jsonElem in jsonArray)
                                        {
                                            if(count > 5)
                                            {
                                                result += "\n...(最多展示 5 条)..."
                                                break
                                            }
                                            val jsonObject = jsonElem.asJsonObject["commit"].asJsonObject
                                            val commitObject = jsonObject["committer"].asJsonObject
                                            result += """
                                                提交者 ${commitObject["name"]} 日期 ${commitObject["date"]}
                                                ${jsonObject["message"]}
                                                
                                                
                                            """.trimIndent()
                                            count++
                                        }
                                        success(datapack,result)
                                    }
                                }
                            }
                        })
                        return
                    }
                }
                argsNotMatch(datapack)
            }
        }
    }
    fun argsNotMatch(datapack : RPCDataPack)
    {
        sendFeedback(datapack,false,"""
                        参数有误，请参考下方指令帮助
                        -
                        github commits {user-name} {repository-name}
                        github commits {user-name} {repository-name}
                        github search-repo {repository-name}
                        github search-user {user-name}
                    """.trimIndent())
    }
    fun failed(datapack: RPCDataPack)
    {
        sendFeedback(datapack,false,"""
                        请求超时，请稍后再试
                    """.trimIndent())
    }
    fun success(datapack: RPCDataPack,message: String)
    {
        sendFeedback(datapack,true,message)
    }

    fun sendFeedback(datapack : RPCDataPack, result : Boolean,message : String)
    {
        MQService.sendCommandFeedBack(
            datapack,
            SimpleCommandResultBody(
                result, """-
${BaseConfiguration.projectChineseName} Github API
-
$message
-
"""
            )
        )
    }
}