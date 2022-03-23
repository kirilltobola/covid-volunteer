package com.isu.covidvolunteer.api


import com.isu.covidvolunteer.models.order.AddOrderDto
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.order.Status
import retrofit2.http.*

interface OrderApi {
    @GET(value = "orders/")
    suspend fun getActuals(
        @Header("Authorization") token: String
    ): List<OrderDto>

    @GET(value = "orders/{id}")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): OrderDto

    @POST(value = "orders/")
    suspend fun addOrder(
        @Header("Authorization") token: String,
        @Body order: AddOrderDto
    )

    @PUT(value = "orders/{id}")
    suspend fun editOrder(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body order: OrderDto
    )

    @PATCH(value = "orders/{id}")
    suspend fun editStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body status: Status
    )

    // TODO: maybe /respond?
    @PATCH(value = "orders/{id}/accept")
    suspend fun respond(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
    @PATCH("orders/{id}/decline")
    suspend fun decline(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )

    @DELETE(value = "orders/{id}")
    suspend fun deleteOrder(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
}