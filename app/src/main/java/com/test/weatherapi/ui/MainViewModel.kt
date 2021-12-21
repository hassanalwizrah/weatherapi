package com.test.weatherapi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.weatherapi.data.entities.DataModel
import com.test.weatherapi.data.remote.ApiResponse
import com.test.weatherapi.domain.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDataUseCase: GetDataUseCase
) : ViewModel() {

    private val _data = MutableStateFlow<ApiResponse<DataModel>>(ApiResponse.Progress(false))
    val data = _data.asStateFlow()

    fun getData() = viewModelScope.launch(IO) {
        getDataUseCase().collectLatest {
            _data.value = it
        }
    }
}