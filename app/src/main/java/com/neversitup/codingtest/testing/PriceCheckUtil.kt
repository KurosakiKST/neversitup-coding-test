package com.neversitup.codingtest.testing

object PriceCheckUtil {
    /**
     * the input is not valid if ...
     * ... amount is empty
     * ... amount is 0
     */

    fun validatePriceCheckInput(
            amount : String,
    ): Boolean {
        if(amount.isEmpty()){
            return false
        }
        if(amount == "0.0"){
            return false
        }
        return true
    }

}