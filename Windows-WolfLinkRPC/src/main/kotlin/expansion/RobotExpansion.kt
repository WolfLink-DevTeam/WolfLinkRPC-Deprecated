package org.wolflink.windows.wolflinkrpc.expansion

import java.awt.Robot

fun Robot.keyClick(key : Int)
{
    this.keyPress(key)
    this.keyRelease(key)
}
fun Robot.mouseClick(key : Int)
{
    this.mousePress(key)
    this.mouseRelease(key)
}