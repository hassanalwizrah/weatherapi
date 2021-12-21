package com.test.weatherapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.weatherapi.databinding.ActivityMainBinding
import com.test.weatherapi.ui.WeatherFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.applyInsetter {
            type(navigationBars = true) {
                margin(bottom = true)
            }
        }

        //TODO: create navigator manger
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainer.id, WeatherFragment())
            .commit()
    }
}