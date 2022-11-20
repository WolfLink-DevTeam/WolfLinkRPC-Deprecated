package org.wolflink.common.wolflinkrpc.api.annotations

/**
 * 在指定包下用 @LocalCallHandler 注解，
 * 声明一个本地Command命令以及其执行函数
 */
@Target(AnnotationTarget.CLASS)
annotation class LocalCallHandler
