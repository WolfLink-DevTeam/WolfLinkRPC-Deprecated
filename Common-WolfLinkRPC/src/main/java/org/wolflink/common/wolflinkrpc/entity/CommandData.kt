package org.wolflink.common.wolflinkrpc.entity

import org.jetbrains.annotations.TestOnly
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.entity.impl.SimpleSender

class CommandData(val commandText : String) {

    val nextCommandList : MutableSet<CommandData> = mutableSetOf()
    var action : (sender : ISender, args : List<String>) -> Boolean = { _, _ ->
        RPCCore.logger.info("unknown command.")
        false}

    companion object{

        private val commandRoot : MutableSet<CommandData> = mutableSetOf()

        @Deprecated("应该由RPCService进行调用，请使用RPCService::analyseCommand")
        fun runCommand(sender : ISender, command : String) : Boolean
        {
            var nowCommandData = CommandData("")
            var tempRoot = commandRoot //根指针
            val commandParts = command.split(" ")
            for (i in commandParts.indices)
            {
                var hasCommand = false
                //寻找是否有这个指令
                for (commandData in tempRoot)
                {
                    if(commandData.commandText == commandParts[i])
                    {
                        tempRoot = commandData.nextCommandList
                        hasCommand = true
                        nowCommandData = commandData
                        break
                    }
                }
                if(!hasCommand) //如果没有这个指令，说明遍历到底了
                {
                    val commandArgs = commandParts.subList(i,commandParts.size)
                    return nowCommandData.action.invoke(sender,commandArgs)
                }
            }
            return nowCommandData.action.invoke(SimpleSender("","",ClientType.UNKNOWN),listOf())
        }
        fun bindCommand(iCommandFunction: ICommandFunction)
        {
            bindCommand(iCommandFunction.getCommand(),iCommandFunction::invoke)
        }
        // 为一串指令绑定action
        fun bindCommand(command: String,action: (sender : ISender, args : List<String>) -> Boolean)
        {
            var tempRoot = commandRoot //根指针
            val commandParts = command.split(" ")
            for (element in commandParts)
            {
                var hasCommand = false
                //寻找是否有这个指令
                for (commandData in tempRoot)
                {
                    if(commandData.commandText == element)
                    {
                        tempRoot = commandData.nextCommandList
                        hasCommand = true
                        break
                    }
                }
                if(!hasCommand) //如果没有这个指令，新建一个
                {
                    val commandData = CommandData(element)
                    tempRoot.add(commandData)
                    tempRoot = commandData.nextCommandList
                    if(element == commandParts.last())commandData.action = action
                }
            }
        }
        private fun showCommand(set : Set<CommandData>)
        {
            for (commandData in set)
            {
                RPCCore.logger.info(commandData.commandText)
                showCommand(commandData.nextCommandList)
            }
        }
        @TestOnly
        fun showCommand() = showCommand(commandRoot)
    }
}