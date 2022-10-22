package com.neversitup.codingtest.view.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neversitup.codingtest.model.local.database.entity.Record
import com.neversitup.codingtest.repository.MainRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
        val repository : MainRepository
) : ViewModel() {

    suspend fun getRecords() = repository.getRecordLists()

}
