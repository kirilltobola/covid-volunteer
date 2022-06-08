package com.isu.covidvolunteer.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.covidvolunteer.api.GenericUserResponse
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.user.UserDto
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.coroutines.launch

class UserViewModel(private var userRepository: UserRepository) : ViewModel() {
    var userOrders: MutableLiveData<GenericUserResponse<List<OrderDto>>> = MutableLiveData()
    var user: MutableLiveData<GenericUserResponse<UserDto>> = MutableLiveData()

    fun getMyOrders(id: Long) {
        viewModelScope.launch {
            userOrders.value = userRepository.getUserOrders(
                "Bearer ${UserDetails.token?.token!!}",
                UserDetails.id!!)
        }
    }

    fun getUser(id: Long) {
        viewModelScope.launch {
            user.value = userRepository.getUser("Bearer ${UserDetails.token?.token!!}", id)
        }
    }
}