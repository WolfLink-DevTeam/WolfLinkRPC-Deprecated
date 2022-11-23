package org.wolflink.windows.wolflinkrpc.entity

import com.sun.jna.platform.win32.User32
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.windows.wolflinkrpc.expansion.keyClick
import org.wolflink.windows.wolflinkrpc.expansion.mouseClick
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.*

data class MCServer(val data : MCServerDataClass){

    private val folderPath = data.folderPath
    private val batName = data.batName
    private val windowTitle = data.windowTitle

    private val batPath = "$folderPath\\$batName"
    private var runTime : Int = 0
    private var timer : Timer = Timer()
    var isRunning : Boolean = false

    //获取运行时间
    fun getRunTimeString() : String
    {
        if(runTime == 0)return "0 小时 0 分钟 0 秒"
        val hour = runTime/3600
        val min = (runTime%3600)/60
        val sec = ((runTime%3600)%60)
        return "$hour 小时 $min 分钟 $sec 秒"
    }

    private fun toggleTimer()
    {
        runTime = 0
        if(!isRunning)//没在运行中
        {
            timer = Timer()
            timer.schedule(object : TimerTask(){
                override fun run() {
                    runTime++
                }
            },1000,1000)
        }
        else//已经在运行了
        {
            timer.cancel()
        }
        isRunning = !isRunning
    }

    // 返回执行结果
    fun startServer() : Pair<Boolean,String>
    {
        if(isRunning)return Pair(false,"服务器已处于运行状态")
        val hwnd = User32.INSTANCE.FindWindow(null,windowTitle)
        if(hwnd != null)return Pair(false,"已存在一个跟服务器窗口标题相同的窗口")
        val diskChar = batPath.first()
        val bodyPath = batPath.substring(0,batPath.lastIndexOf('\\'))
        val batName = batPath.substring(batPath.lastIndexOf('\\')+1)
        Runtime.getRuntime().exec("cmd /c cd $bodyPath & $diskChar: & start $batName")
        toggleTimer()
        return Pair(true,"服务器启动成功")
    }
    // 返回执行结果
    fun stopServer() : Pair<Boolean,String>
    {
        if(!isRunning)return Pair(false,"服务器未处于运行中")
        val hwnd = User32.INSTANCE.FindWindow(null,windowTitle)
        if(hwnd == null)
        {
            isRunning = false
            toggleTimer()
            return Pair(false,"未找到窗口 $windowTitle")
        }
        GlobalScope.launch {
            User32.INSTANCE.SetForegroundWindow(hwnd)
            User32.INSTANCE.MoveWindow(hwnd,0,0,800,600,true)
            delay(500)
            val robot = Robot()
            robot.mouseMove(5,5)
            delay(100)
            robot.mouseClick(InputEvent.BUTTON1_DOWN_MASK)
            robot.keyClick(KeyEvent.VK_S)
            delay(100)
            robot.keyClick(KeyEvent.VK_T)
            delay(100)
            robot.keyClick(KeyEvent.VK_O)
            delay(100)
            robot.keyClick(KeyEvent.VK_P)
            delay(100)
            robot.keyClick(KeyEvent.VK_ENTER)
        }
        toggleTimer()
        return Pair(true,"服务器关闭成功")
    }
}