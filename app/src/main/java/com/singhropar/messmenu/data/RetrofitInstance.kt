package com.singhropar.messmenu.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: MessApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://r2.singhropar.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MessApi::class.java)
    }
}
