package com.magicapp.exam_modul_6_cards_kotlin.networking.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    var BASE_URl = "https://623303e86de3467dbac5eb3d.mockapi.io/"

    private val retrofit = getRetrofit()


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URl)
            .build()
    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}