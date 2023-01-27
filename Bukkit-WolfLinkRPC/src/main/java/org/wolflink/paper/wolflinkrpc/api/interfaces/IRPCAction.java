package org.wolflink.paper.wolflinkrpc.api.interfaces;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;

public interface IRPCAction extends Function1<RPCDataPack, Unit> {
}
