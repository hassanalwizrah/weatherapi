package com.test.weatherapi.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Flow<T>.safeCollection(lifecycleOwner: LifecycleOwner, data: suspend (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        this@safeCollection.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            data(it)
        }
    }
}