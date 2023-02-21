package com.example.mercedesbenzapp.utils

import com.example.mercedesbenzapp.model.domain.RestaurantDomain


sealed class UIState<out T> {
    object LOADING : UIState<Nothing>()
    data class SUCCESS<T>(val response: T) : UIState<T>()
    data class ERROR(val error: Exception) : UIState<Nothing>()
}