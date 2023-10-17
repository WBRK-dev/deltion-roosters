package com.wbrk.deltionroosters.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface apiInterface {
    @GET("/roster/day")
    suspend fun getRosterDay(@Query("group") group: String, @Query("day") day: Int) : Response<Week>

    @GET("/roster")
    suspend fun getRosterWeek(@Query("group") group: String, @Query("week") week: Int) : Response<Week>
}