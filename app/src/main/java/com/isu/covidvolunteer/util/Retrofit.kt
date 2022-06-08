package com.isu.covidvolunteer.util

import com.isu.covidvolunteer.BuildConfig
import com.isu.covidvolunteer.api.AuthApi
import com.isu.covidvolunteer.api.ChatApi
import com.isu.covidvolunteer.api.OrderApi
import com.isu.covidvolunteer.api.UserApi
import com.isu.covidvolunteer.retrofit.CustomResponseCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object Retrofit {
    private const val BASE_URL = BuildConfig.API_URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CustomResponseCallAdapterFactory())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val chatApi: ChatApi by lazy {
        retrofit.create(ChatApi::class.java)
    }

    val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}