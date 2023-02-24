package com.example.mercedesbenzapp.utils

import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.model.domain.UserDomain

sealed class ViewType {
    data class RESTAURANT(val restaurantList: RestaurantDomain) : ViewType()
    data class USER(val userList: UserDomain) : ViewType()
}
