package org.wolflink.common.wolflinkrpc.api.enums

enum class DataPackType(val needFeedback : Boolean) {
    COMMAND_EXECUTE(true),COMMAND_RESULT(false),TEXT_MESSAGE(false)
}