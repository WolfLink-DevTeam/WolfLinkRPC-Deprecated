package org.wolflink.common.wolflinkrpc.api.enums

enum class PermissionLevel(val level : Int) {
    DEFAULT(1),ADMIN(2),OWNER(3);
}
infix fun PermissionLevel.reach(another : PermissionLevel) : Boolean = this.level >= another.level
infix fun PermissionLevel.reach(anotherInt : Int) : Boolean = this.level >= anotherInt