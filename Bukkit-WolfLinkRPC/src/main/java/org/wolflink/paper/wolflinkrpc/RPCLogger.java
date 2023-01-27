package org.wolflink.paper.wolflinkrpc;

import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.interfaces.ILogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RPCLogger implements ILogger {

    Logger logger = App.INSTANCE.getLogger();

    @Override
    public void debug(@NotNull String str) {
        if(App.RPC_CONFIGURATION.getDebug()) logger.log(Level.INFO,str);
    }

    @Override
    public void info(@NotNull String str) {
        logger.log(Level.INFO,str);
    }

    @Override
    public void warn(@NotNull String str) {
        logger.log(Level.WARNING,str);
    }

    @Override
    public void error(@NotNull String str) {
        logger.log(Level.SEVERE,str);
    }
}
