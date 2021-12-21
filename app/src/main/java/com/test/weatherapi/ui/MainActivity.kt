package com.test.weatherapi.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.test.weatherapi.data.remote.ApiResponse
import com.test.weatherapi.databinding.ActivityMainBinding
import com.test.weatherapi.utils.safeCollection
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            viewModel.getData()
        }

        viewModel.data.safeCollection(this) {
            when(it) {
                is ApiResponse.Error -> {
                    binding.textview.text = it.errorMessage
                    Timber.e("${it.errorCode}: ${it.errorMessage}")
                }
                is ApiResponse.Progress -> {
                    binding.loading.isVisible = it.progress
                }
                is ApiResponse.Success -> {
                    binding.textview.text = it.items?.current?.temp_c?.toString().orEmpty()
                }
            }
        }
    }
}