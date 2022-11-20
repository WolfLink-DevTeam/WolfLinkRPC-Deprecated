package org.wolflink.common.wolflinkrpc.api.annotations

/**
 * 在指定包下使用 @RemoteCallHandler 注解，
 * 以声明一个用于处理远程数据包数据的分析类
 */
@Target(AnnotationTarget.CLASS)
annotation class RemoteCallHandler