import kotlinx.coroutines.delay
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ITextMessageBody
import org.wolflink.common.wolflinkrpc.entity.CommandData
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.service.MQService
import java.util.Scanner

class Test{}

fun main()
{

//    RPCCore.initSystem(MyConfiguration)
//
//    val textMessageBody : ITextMessageBody = object : ITextMessageBody{
//        override fun getMsg(): String = "这是一段纯文本消息哦"
//    }
//
//    val datapack = RPCDataPack.Builder()
//        .setSenderName(MyConfiguration.getQueueName())
//        .setType(DataPackType.COMMAND_EXECUTE)
//        .setDatapackBody(textMessageBody)
//        .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE).addRoutingKey("bukkit_test"))
//        .build()
//
//    val callbackFunction = object : CallbackFunction {
//        override fun success(datapack: RPCDataPack) {
//            RPCCore.logger.debug("Get datapack feedback ! JsonBody: ${datapack.jsonObject}")
//        }
//        override fun failed(datapack: RPCDataPack) {
//            RPCCore.logger.debug("Cannot get datapack feedback within specified seconds.")
//        }
//    }
//    MQService.sendDataPack(datapack,true,callbackFunction,10)
}