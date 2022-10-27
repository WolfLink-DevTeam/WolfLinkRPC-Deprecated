package org.wolflink.common.wolflinkrpc.service

import org.jetbrains.annotations.TestOnly
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel
import org.wolflink.common.wolflinkrpc.api.enums.notReach
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ISender
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ICommandFunction
import org.wolflink.common.wolflinkrpc.entity.CommandData
import org.wolflink.common.wolflinkrpc.utils.ReflectionUtil

object CommandService {

    private val commandRoot : MutableSet<CommandData> = mutableSetOf()

    //初始化指令部分，用反射实现
    fun init(configuration: IConfiguration)
    {
        var count = 0
        val classes = ReflectionUtil.getClassesByAnnotation(configuration.getCommandFunctionPackage(), CommandFunction::class.java)
        for (clazz in classes)
        {
            val iCommandFunction = clazz.getConstructor().newInstance() as ICommandFunction
            bindCommand(iCommandFunction)
            count++
        }
        RPCCore.logger.info("CommandData has been initialized , $count commands are now available.")
    }


    fun listSubCommand(command : String) : Set<CommandData>
    {
        val commandData = findCommand(command).second
        return commandData.nextCommandList
    }

    // int是命令层级，从0层开始，0层就是不存在的，1层是 > 等根指令
    fun findCommand(command : String) : Pair<Int,CommandData> {
        var nowCommandData = CommandData("")
        var tempRoot = commandRoot //根指针
        val commandParts = command.split(" ")
        for (i in commandParts.indices) {
            var hasCommand = false
            //寻找是否有这个指令
            for (commandData in tempRoot) {
                if (commandData.commandText == commandParts[i]) {
                    tempRoot = commandData.nextCommandList
                    hasCommand = true
                    nowCommandData = commandData
                    break
                }
            }
            if (!hasCommand) //如果没有这个指令，说明遍历到底了
            {
                return Pair(i,nowCommandData)
            }
        }
        return Pair(commandParts.size,nowCommandData)
    }

    @Deprecated("应该由RPCService进行调用，请使用RPCService::analyseCommand")
    fun runCommand(sender : ISender, command : String) : Pair<Boolean,String>
    {
        val (index,commandData) = findCommand(command)

        RPCCore.logger.debug("find command ${commandData.commandText}")

        //TODO 阻止调用，但是未通知阻止原因，可以换用 Pair(Boolean,String)
        if(sender.getPermission() notReach commandData.permission) return Pair(false,"Permission denied , require ${commandData.permission.name}")
        val commandParts = command.split(" ")
        val commandArgs = commandParts.subList(index,commandParts.size)

        RPCCore.logger.debug("origin command : $command command args : $commandArgs")
        return commandData.action.invoke(sender,commandArgs)

//        var nowCommandData = CommandData("")
//        var tempRoot = commandRoot //根指针
//        val commandParts = command.split(" ")
//        for (i in commandParts.indices)
//        {
//            var hasCommand = false
//            //寻找是否有这个指令
//            for (commandData in tempRoot)
//            {
//                if(commandData.commandText == commandParts[i])
//                {
//                    tempRoot = commandData.nextCommandList
//                    hasCommand = true
//                    nowCommandData = commandData
//                    break
//                }
//            }
//            if(!hasCommand) //如果没有这个指令，说明遍历到底了
//            {
//                val commandArgs = commandParts.subList(i,commandParts.size)
//                return nowCommandData.action.invoke(sender,commandArgs)
//            }
//        }
//        return nowCommandData.action.invoke(SimpleSender("","", ClientType.UNKNOWN),listOf())
    }
    fun bindCommand(iCommandFunction: ICommandFunction)
    {
        bindCommand(iCommandFunction.getCommand(),iCommandFunction::invoke,iCommandFunction.getPermission())
    }
    // 为一串指令绑定action
    private fun bindCommand(command: String,action: (sender : ISender, args : List<String>) -> Pair<Boolean,String>,permission : PermissionLevel)
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
                val commandData = CommandData(element,permission)
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