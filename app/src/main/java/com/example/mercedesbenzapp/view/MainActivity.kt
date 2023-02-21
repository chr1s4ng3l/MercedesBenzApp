package com.example.mercedesbenzapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.viewmodel.MercedesViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mercedesViewModel: MercedesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mercedesViewModel.getAllBusinessByLoc(33.9840285, -84.4269436)
        mercedesViewModel.business.observe(this, Observer {
            println("MainActivity.onCreate  $it")
            Log.d(TAG, "onCreate: $it")
        })
    }
}