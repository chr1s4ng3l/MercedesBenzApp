package com.example.mercedesbenzapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.model.domain.UserDomain
import com.example.mercedesbenzapp.rest.MercedesRepositoryImpl
import com.example.mercedesbenzapp.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MercedesViewModel @Inject constructor(
    private val mercedesRepository: MercedesRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {


    var name = ""
    var id = ""
    var image = ""
    var phone = ""
    var price = ""
    var distance = 0.0
    var rating = 0.0

    private val _business: MutableLiveData<UIState<List<RestaurantDomain>>> =
        MutableLiveData(UIState.LOADING)
    val business: LiveData<UIState<List<RestaurantDomain>>> get() = _business
    private val _users: MutableLiveData<UIState<List<UserDomain>>> =
        MutableLiveData(UIState.LOADING)
    val users: LiveData<UIState<List<UserDomain>>> get() = _users
    var isLoading = MutableLiveData<Boolean>()


    init {

        getAllBusinessByLoc()
        getUsersReviews()
    }

     fun getUsersReviews(id: String? = null) {
        id?.let {
            viewModelScope.launch(ioDispatcher) {
                mercedesRepository.getUsers(id).collect {
                    _users.postValue(it)
                    Log.d(ContentValues.TAG, "TEST: $_business")
                }
            }
        }
    }

     fun getAllBusinessByLoc(lat: Double? = null, lon: Double? = null) {
        if (lat != null && lon != null) {
            viewModelScope.launch(ioDispatcher) {
                mercedesRepository.getRestaurants(lat, lon).collect {
                    _business.postValue(it)
                    Log.d(ContentValues.TAG, "TEST: $_business")
                }
            }
        }

    }


}