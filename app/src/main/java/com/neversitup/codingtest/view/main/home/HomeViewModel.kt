package com.neversitup.codingtest.view.main.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neversitup.codingtest.application.MyApplication
import com.neversitup.codingtest.model.local.database.entity.Record
import com.neversitup.codingtest.model.remote.CurrentBTCPricesResponse
import com.neversitup.codingtest.repository.MainRepository
import com.neversitup.codingtest.utility.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HomeViewModel(
        val repository : MainRepository
) : ViewModel() {
    val currentBTCPrices : MutableLiveData<Resource<CurrentBTCPricesResponse>> = MutableLiveData()
    val currentBTCPricesResponse : CurrentBTCPricesResponse? = null

    fun getCurrentPricesForBTC() = viewModelScope.launch {
        safeCurrentPricesCall()
    }

    private fun handleCurrentPricesResponse(response : Response<CurrentBTCPricesResponse>) : Resource<CurrentBTCPricesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(currentBTCPricesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeCurrentPricesCall() {
        currentBTCPrices.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getCurrentPricesForBTC()
                currentBTCPrices.postValue(handleCurrentPricesResponse(response))
            }
            else {
                currentBTCPrices.postValue(Resource.Error("No internet connection"))
            }
        }
        catch (t : Throwable) {
            when (t) {
                is IOException -> currentBTCPrices.postValue(Resource.Error("Network Failure"))
                else -> currentBTCPrices.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun saveRecord(record : Record) = viewModelScope.launch {
        repository.upsert(record)
    }

    private fun hasInternetConnection() : Boolean {
        val connectivityManager = MyApplication.instance.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
