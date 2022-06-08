package com.isu.covidvolunteer.retrofit

import android.util.Log
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

internal class CustomResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<CustomResponse<S, E>> {

    override fun clone(): Call<CustomResponse<S, E>> {
        return CustomResponseCall(delegate.clone(), errorConverter)
    }

    override fun execute(): Response<CustomResponse<S, E>> {
        throw UnsupportedOperationException("Doesn't support execution")
    }

    override fun enqueue(callback: Callback<CustomResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val code = response.code()
                val body = response.body()
                val error = response.errorBody()
                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@CustomResponseCall,
                            Response.success(CustomResponse.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@CustomResponseCall,
                            Response.success(CustomResponse.UnexpectedError(null))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> {
                            try {
                                errorConverter.convert(error)
                            } catch (e: Exception) {
                                Log.d("ERROR_CONVERTER", e.toString())
                                null
                            }
                        }
                    }

                    if (errorBody != null) {
                        callback.onResponse(
                            this@CustomResponseCall,
                            Response.success(CustomResponse.ApiError(errorBody, code))
                        )
                    } else {
                        callback.onResponse(
                            this@CustomResponseCall,
                            Response.success(CustomResponse.UnexpectedError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                val response = when (t) {
                    is IOException -> CustomResponse.NetworkError(t)
                    else -> CustomResponse.UnexpectedError(t)
                }
                callback.onResponse(
                    this@CustomResponseCall,
                    Response.success(response)
                )
            }

        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}