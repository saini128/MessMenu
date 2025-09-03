package com.singhropar.messmenu.data

import retrofit2.http.GET

interface MessApi {
    @GET("mess.json")
    suspend fun getMessMenu(): List<MessMenuItem>
}
