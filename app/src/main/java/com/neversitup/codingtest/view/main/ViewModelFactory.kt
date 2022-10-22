package com.neversitup.codingtest.view.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neversitup.codingtest.repository.MainRepository
import com.neversitup.codingtest.view.main.history.HistoryViewModel
import com.neversitup.codingtest.view.main.home.HomeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory constructor(
        val repository : MainRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            HistoryViewModel(repository) as T
        }
        else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}