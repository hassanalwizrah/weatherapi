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
/*
    private val viewModel by activityViewModels<MainViewModel>()
    private val viewModel: MainViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
 */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            viewModel.getData()
        }

        viewModel.data.safeCollection(this) {
            when (it) {
                is ApiResponse.Error -> it.requestException.apply {
                    binding.textview.text = message
                    Timber.e("$code/$bodyErrorCode: $message")
                }
                is ApiResponse.Progress -> {
                    binding.loading.isVisible = it.progress
                    binding.textview.isVisible = !it.progress
                }
                is ApiResponse.Success -> it.items?.apply {
                    binding.textview.text = current?.temp_c?.toString().orEmpty()
                }
            }
        }
    }
}