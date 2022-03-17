package com.magicapp.exam_modul_6_cards_kotlin.networking.service

import com.magicapp.exam_modul_6_cards_kotlin.model.Cards
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@JvmSuppressWildcards
interface CardService {

    @GET("cards")
    fun getCards(): Call<List<Cards>>

    @POST("cards")
    fun addCard(@Body cards: Cards): Call<Cards>
}