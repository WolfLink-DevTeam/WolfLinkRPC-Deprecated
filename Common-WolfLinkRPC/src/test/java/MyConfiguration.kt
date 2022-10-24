import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.enums.ClientType
import org.wolflink.common.wolflinkrpc.api.enums.DataPackType
import org.wolflink.common.wolflinkrpc.api.enums.ExchangeType
import org.wolflink.common.wolflinkrpc.api.interfaces.IConfiguration
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger
import org.wolflink.common.wolflinkrpc.api.interfaces.analyse.IAnalyse
import org.wolflink.common.wolflinkrpc.api.interfaces.datapack.ICommandResultBody
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack
import org.wolflink.common.wolflinkrpc.entity.RoutingData
import org.wolflink.common.wolflinkrpc.service.MQService

object MyConfiguration : IConfiguration {
    override fun getHost(): String = "43.248.79.66"


    override fun getPort(): Int = 55559

    override fun getUsername(): String = "testuser"

    override fun getPassword(): String = "Abc233"

    override fun getQueueName(): String = "bukkit_test"

    override fun getLogger(): ILogger = MyLogger
    override fun getClientType(): ClientType = ClientType.BUKKIT
    override fun getAnalyseFunctionPackage(): String = "org.wolflink.mirai.wolflinkrpc.analyse"

    override fun getMainClass(): Class<*> = RPCCore::class.java
}