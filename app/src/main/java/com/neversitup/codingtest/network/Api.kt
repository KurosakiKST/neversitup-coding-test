package com.neversitup.codingtest.network

import com.neversitup.codingtest.model.remote.CurrentBTCPricesResponse
import com.neversitup.codingtest.utility.Constants
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET(Constants.CURRENT_PRICE_END_POINT)
    suspend fun getCurrentPricesForBTC() : Response<CurrentBTCPricesResponse>
}