package com.neversitup.codingtest.view.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.neversitup.codingtest.R
import com.neversitup.codingtest.databinding.FragmentHistoryBinding
import com.neversitup.codingtest.model.local.database.AppDatabase
import com.neversitup.codingtest.model.local.database.entity.Record
import com.neversitup.codingtest.network.RetrofitInstance
import com.neversitup.codingtest.repository.MainRepository
import com.neversitup.codingtest.view.main.ViewModelFactory
import com.neversitup.codingtest.view.main.home.HomeViewModel
import kotlinx.coroutines.launch
import java.util.Collections.list

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private lateinit var binding : FragmentHistoryBinding

    lateinit var viewModel : HistoryViewModel

    private lateinit var historyAdapter : HistoryAdapter

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val newsRepository = MainRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HistoryViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val records = viewModel.getRecords()
            binding.rvHistory.layoutManager = LinearLayoutManager(activity)
            historyAdapter = HistoryAdapter(records)
            binding.rvHistory.adapter = historyAdapter
        }

    }

}