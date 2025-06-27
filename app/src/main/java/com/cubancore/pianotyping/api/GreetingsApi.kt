package com.cubancore.pianotyping.api

import com.cubancore.pianotyping.data.Greeting
import retrofit2.Call
import retrofit2.http.GET

interface GreetingsApi {
    @GET("greeting/")
    fun fetchGreeting(): Call<Greeting>
}