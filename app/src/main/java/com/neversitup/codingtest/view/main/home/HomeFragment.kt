package com.neversitup.codingtest.view.main.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.*
import com.neversitup.codingtest.R
import com.neversitup.codingtest.databinding.FragmentHomeBinding
import com.neversitup.codingtest.model.local.database.AppDatabase
import com.neversitup.codingtest.model.local.database.entity.Record
import com.neversitup.codingtest.repository.MainRepository
import com.neversitup.codingtest.testing.PriceCheckUtil
import com.neversitup.codingtest.utility.Resource
import com.neversitup.codingtest.view.main.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    private lateinit var viewModel : HomeViewModel

    private val amountLiveData = MutableLiveData<String>()
    private val bpiLiveData = MutableLiveData<String>()
    private val isValidLiveData = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(amountLiveData) { amount ->
            this.value = PriceCheckUtil.validatePriceCheckInput(amount)
        }
    }

    private var amountInBTC : Double = 0.0

    private var usdRate : Double = 0.0
    private var gbpRate : Double = 0.0
    private var eurRate : Double = 0.0
    private var chartName : String = ""

    //Loop 1 min
    var handler : Handler = Handler()
    var runnable : Runnable? = null
    var delay = 60000

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val newsRepository = MainRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentPricesForBTC()

        viewModel.currentBTCPrices.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        binding.lblCurrentPriceForBtc.text = "Current price for ${it.chartName}"
                        binding.lblUsd.text = it.bpi.USD.code
                        binding.lblGbp.text = it.bpi.GBP.code
                        binding.lblEur.text = it.bpi.EUR.code
                        binding.tvUsdValue.text = it.bpi.USD.rate
                        binding.tvGbpValue.text = it.bpi.GBP.rate
                        binding.tvEurValue.text = it.bpi.EUR.rate

                        chartName = it.chartName
                        usdRate = it.bpi.USD.rate_float
                        gbpRate = it.bpi.GBP.rate_float
                        eurRate = it.bpi.EUR.rate_float

                        binding.lblCheckPriceBtc.text = "Check price in ${it.chartName}"

                        binding.tvAmountInBtc.text = "Input amount in ${it.chartName} is"

                        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                        val currentTime = LocalDateTime.now().format(formatter)
                        val record = Record(0,
                            currentTime,
                            it.bpi.USD.rate,
                            it.bpi.GBP.rate,
                            it.bpi.EUR.rate
                        )

                        viewModel.saveRecord(record)

                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        binding.etAmount.doOnTextChanged { text, _, _, _ ->
            amountLiveData.value = text?.toString()
        }

        val bpis = resources.getStringArray(R.array.bpis)

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, bpis)
        binding.spCurrency.adapter = spinnerAdapter
        binding.spCurrency.setSelection(0)

        binding.spCurrency.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent : AdapterView<*>?, view : View?, position : Int, id : Long) {
                if (parent !!.getItemAtPosition(position).equals("USD")) {
                    bpiLiveData.value = "USD"
                }
                else if (parent.getItemAtPosition(position).equals("GBP")) {
                    bpiLiveData.value = "GBP"
                }
                else if (parent.getItemAtPosition(position).equals("EUR")) {
                    bpiLiveData.value = "EUR"
                }

            }

            override fun onNothingSelected(parent : AdapterView<*>?) {
                binding.spCurrency.setSelection(0)
            }

        }

        isValidLiveData.observe(viewLifecycleOwner) { isValid ->
            binding.btnCheck.isEnabled = isValid
        }

        binding.btnCheck.setOnClickListener {
            val amountInDouble = amountLiveData.value?.toDouble()
            if (amountInDouble != null) {
                when (bpiLiveData.value) {
                    "USD" -> {
                        amountInBTC = rateCalculator(amountInDouble, usdRate)
                    }

                    "GBP" -> {
                        amountInBTC = rateCalculator(amountInDouble, gbpRate)
                    }

                    "EUR" -> {
                        amountInBTC = rateCalculator(amountInDouble, eurRate)
                    }
                }

                Log.i("btc", amountInBTC.toString())
                binding.tvAmountInBtc.text = "Input amount in $chartName is $amountInBTC"
            }
        }

    }

    private fun hideProgressBar() {
        binding.loadingBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.loadingBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable !!, delay.toLong())
            viewModel.getCurrentPricesForBTC()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable !!)
    }

    private fun rateCalculator(amount : Double, rate : Double) : Double {
        return (amount.div(rate))
    }
}