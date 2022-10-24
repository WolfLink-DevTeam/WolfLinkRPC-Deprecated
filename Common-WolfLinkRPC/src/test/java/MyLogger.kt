import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger

object MyLogger : ILogger {

    val prefix = "[ WolfLinkRPC ]"

    override fun debug(str: String) {
        println("$prefix [debug] $str")
    }

    override fun info(str: String) {
        println("$prefix [info] $str")
    }

    override fun warn(str: String) {
        println("$prefix [warn] $str")
    }

    override fun error(str: String) {
        println("$prefix [error] $str")
    }
}