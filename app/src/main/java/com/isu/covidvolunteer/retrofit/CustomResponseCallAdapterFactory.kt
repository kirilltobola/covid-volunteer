package com.isu.covidvolunteer.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CustomResponseCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (getRawType(returnType) != Call::class.java) return null


        check(returnType is ParameterizedType) {
            "Return type must be parameterized as Call<Response<<Foo>> or Call<Response<out Foo>>"
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != CustomResponse::class.java) return null

        check(responseType is ParameterizedType) {
            "Response must be parameterized as Response<Foo> or Response<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter = retrofit.nextResponseBodyConverter<Any>(
            null, errorBodyType, annotations
        )
        return CustomResponseCallAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }
}