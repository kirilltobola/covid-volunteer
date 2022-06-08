package com.isu.covidvolunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.google.gson.Gson
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.util.UserDetails

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        checkIfUserDetailsSaved()
    }

    private fun checkIfUserDetailsSaved() {
        val sharedPreferences = getSharedPreferences("VOLUNTEER_API", MODE_PRIVATE)
        val spAuthResponse = sharedPreferences.getString("UserDetails", null)
        if (!spAuthResponse.isNullOrEmpty()) {
            val authResponse: AuthResponse? = Gson().fromJson(
                spAuthResponse,
                AuthResponse::class.java
            )
            if (authResponse != null) {
                UserDetails.id = authResponse.id
                UserDetails.token = authResponse.token
                UserDetails.roles = authResponse.roles
            }
        } else {
            UserDetails.id = null
            UserDetails.token = null
            UserDetails.roles = null
        }
    }
}