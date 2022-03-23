package com.isu.covidvolunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.util.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}