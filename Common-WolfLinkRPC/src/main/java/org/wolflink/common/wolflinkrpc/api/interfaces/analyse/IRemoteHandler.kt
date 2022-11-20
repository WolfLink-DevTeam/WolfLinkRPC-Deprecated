package org.wolflink.common.wolflinkrpc.api.interfaces.analyse

interface IRemoteHandler {
    fun getPredicate() : IPredicate
    fun getAction() : IAction
}