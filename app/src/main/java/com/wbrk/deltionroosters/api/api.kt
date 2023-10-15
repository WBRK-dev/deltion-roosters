package com.wbrk.deltionroosters.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object api {
    val baseUrl = "https://deltion-roosters-api.vercel.app/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}