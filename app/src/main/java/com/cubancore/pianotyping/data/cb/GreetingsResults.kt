package com.cubancore.pianotyping.data.cb

import com.cubancore.pianotyping.data.Greeting

interface GreetingsResults {
    fun onDataFetchedSuccess(message : Greeting)

    fun onDataFetchedFailed(message: String)
}