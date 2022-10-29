import java.io.File

class Test{}

fun main()
{
    val file = File("E:\\")
    println("file exist ? ${file.exists()}")
    val files = file.listFiles()
    for (f in files)
    {
        println(f.path)
    }


//    RPCCore.initSystem(MyConfiguration)

//    val commandBody = SimpleCommandExecuteBody("测试")
//    val datapack = RPCDataPack.Builder()
//        .setType(DataPackType.COMMAND_EXECUTE)
//        .setDatapackBody(commandBody)
//        .addRoutingData(RoutingData(ExchangeType.SINGLE_EXCHANGE, mutableListOf("paper_1")))
//        .build()
//    val callback = object : CallbackFunction{
//    }
//    MQService.sendDataPack(datapack,true,callback,10)
//    val scanner = Scanner(System.`in`)
//    var test = scanner.nextInt()
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