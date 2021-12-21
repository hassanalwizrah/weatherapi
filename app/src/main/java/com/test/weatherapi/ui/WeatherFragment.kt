package com.test.weatherapi.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.test.weatherapi.R
import com.test.weatherapi.base.BaseFragment
import com.test.weatherapi.data.remote.ApiResponse
import com.test.weatherapi.databinding.FragmentWeatherBinding
import com.test.weatherapi.utils.safeCollection
import com.test.weatherapi.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    /**
     *   other ways to create viewModel:
     *   - viewModel by activityViewModels<WeatherViewModel>()
     *   - viewModel by viewModels<WeatherViewModel>(ownerProducer = { requireParentFragment() })
     *
     */
    private val viewModel by viewModels<WeatherViewModel>()
    private val binding by viewBinding(FragmentWeatherBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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