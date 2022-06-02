package com.don2.shopintelli

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageSender {
    @POST("webhook")
    fun sendMessage(@Body userMessage: UserMessage?): Call<List<com.don2.shopintelli.BotResponse>>
}