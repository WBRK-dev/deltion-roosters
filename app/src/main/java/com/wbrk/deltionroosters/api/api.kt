package com.wbrk.deltionroosters.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object api {
    var baseUrl = "https://deltion-roosters-api.vercel.app/"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(url: String): Retrofit {
        if (url !== "") {
            baseUrl = url
        }
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}