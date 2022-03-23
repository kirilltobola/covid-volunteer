package com.isu.covidvolunteer.repository

import com.isu.covidvolunteer.api.OrderApi
import com.isu.covidvolunteer.models.order.AddOrderDto
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.order.Status
import com.isu.covidvolunteer.util.Retrofit

class OrderRepository : OrderApi{
    override suspend fun getActuals(token: String): List<OrderDto> {
        return Retrofit.orderApi.getActuals(token)
    }

    override suspend fun getOrder(token: String, id: Long): OrderDto {
        return Retrofit.orderApi.getOrder(token, id)
    }

    override suspend fun addOrder(token: String, order: AddOrderDto) {
        return Retrofit.orderApi.addOrder(token, order)
    }

    override suspend fun editOrder(token: String, id: Int, order: OrderDto) {
        return Retrofit.orderApi.editOrder(token, id, order)
    }

    override suspend fun editStatus(token: String, id: Int, status: Status) {
        return Retrofit.orderApi.editStatus(token, id, status)
    }

    override suspend fun respond(token: String, id: Long) {
        return Retrofit.orderApi.respond(token, id)
    }

    override suspend fun decline(token: String, id: Long) {
        return Retrofit.orderApi.decline(token, id)
    }

    override suspend fun deleteOrder(token: String, id: Long) {
        return Retrofit.orderApi.deleteOrder(token, id)
    }
}