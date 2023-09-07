package com.example.b3networkstest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiEndpoint {

    @POST("")
    suspend fun getDataAsync(
        @Body request: DataRequest,
    ): Response<Unit>
}