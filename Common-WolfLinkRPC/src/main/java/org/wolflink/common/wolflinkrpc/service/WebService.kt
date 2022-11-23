package org.wolflink.common.wolflinkrpc.service

import okhttp3.OkHttpClient
import java.time.Duration
object WebService {
    val httpClient = OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(5))
        .callTimeout(Duration.ofSeconds(5))
        .readTimeout(Duration.ofSeconds(5))
        .writeTimeout(Duration.ofSeconds(5))
        .build()
}