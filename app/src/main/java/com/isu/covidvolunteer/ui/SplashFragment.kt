package com.isu.covidvolunteer.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.util.UserDetails

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(UserDetails.isTokenValid()) {
            view.postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            }, 3000)
        } else {
            view.postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }, 3000)
        }
    }
}