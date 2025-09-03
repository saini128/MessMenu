package com.singhropar.messmenu.data

class MessRepository {
    suspend fun getMenu(): List<MessMenuItem> {
        return RetrofitInstance.api.getMessMenu()
    }
}
