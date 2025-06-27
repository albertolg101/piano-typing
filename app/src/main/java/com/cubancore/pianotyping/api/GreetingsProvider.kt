package com.cubancore.pianotyping.api

import com.cubancore.pianotyping.data.Greeting
import com.cubancore.pianotyping.data.cb.GreetingsResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

private const val BASE_URL = "http://194.163.172.93:8280/"

private val retrofit by lazy {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create<GreetingsApi>()
}

class GreetingsProvider() {
    fun fetchGreeting(cb: GreetingsResults) {
        retrofit.fetchGreeting().enqueue(object : Callback<Greeting> {
            override fun onResponse(
                call: Call<Greeting>,
                response: Response<Greeting>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    cb.onDataFetchedSuccess(response.body()!!)
                } else {
                    cb.onDataFetchedFailed("${response.code()}: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Greeting>, t: Throwable) {
                cb.onDataFetchedFailed("Error: ${t.message}")
            }
        })
    }
}