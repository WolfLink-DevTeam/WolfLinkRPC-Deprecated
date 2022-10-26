package org.wolflink.common.wolflinkrpc.api.enums

enum class PermissionLevel(val level : Int) {
    DEFAULT(1), //默认权限组
    API_USER(3), //可以调用一些功能性、娱乐性API
    ADMIN(5), //可以操作深层指令，但不应该拥有框架、系统操作权
    OWNER(10), //全部指令
    ONLY_MANAGER(100) //唯一管理者，可以为其他用户设置管理员权限
}
infix fun PermissionLevel.reach(another : PermissionLevel) : Boolean = this.level >= another.level
infix fun PermissionLevel.reach(anotherInt : Int) : Boolean = this.level >= anotherInt
infix fun PermissionLevel.notReach(another: PermissionLevel) : Boolean = this.level < another.level
infix fun PermissionLevel.notReach(anotherInt: Int) : Boolean = this.level < anotherInt