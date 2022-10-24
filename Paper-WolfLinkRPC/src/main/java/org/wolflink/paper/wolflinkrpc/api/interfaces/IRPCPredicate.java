package org.wolflink.paper.wolflinkrpc.api.interfaces;

import kotlin.jvm.functions.Function1;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;

public interface IRPCPredicate extends Function1<RPCDataPack, Boolean> {

}
