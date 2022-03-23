package com.isu.covidvolunteer.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.covidvolunteer.models.order.AddOrderDto
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    var activeOrders: MutableLiveData<List<OrderDto>> = MutableLiveData()
    var myOrders: MutableLiveData<List<OrderDto>> = MutableLiveData() // TODO: delete
    var order: MutableLiveData<OrderDto> = MutableLiveData()

    fun getActiveOrders() {
        viewModelScope.launch {
            activeOrders.value = orderRepository.getActuals("Bearer ${UserDetails.token?.token!!}") // TODO: improve it
        }
    }

    fun getOrder(id: Long) {
        viewModelScope.launch {
            order.value = orderRepository.getOrder("Bearer ${UserDetails.token?.token!!}", id)
        }
    }

    fun addOrder(order: AddOrderDto) {
        viewModelScope.launch {
            orderRepository.addOrder("Bearer ${UserDetails.token?.token!!}", order)
        }
    }

    fun respondToOrder(id: Long) {
        viewModelScope.launch {
            orderRepository.respond("Bearer ${UserDetails.token?.token!!}", id)
        }
    }

    fun decline(id: Long) {
        viewModelScope.launch {
            orderRepository.decline("Bearer ${UserDetails.token?.token!!}", id)
        }
    }
}