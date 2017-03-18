package com.importre.example.api

import com.importre.example.model.Photos
import com.importre.example.model.Users
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    companion object {
        val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    @GET("users")
    fun getUsers(): Observable<Users>

    @GET("photos")
    fun getPhotos(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 30
    ): Observable<Response<Photos>>
}
