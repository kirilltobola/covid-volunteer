package com.isu.covidvolunteer.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.role.RoleDto
import com.isu.covidvolunteer.util.UserDetails

class MainFragment : Fragment(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(UserDetails.hasRole(RoleDto("ROLE_MEDIC"))) {
            bottomNavigation.menu.clear()
            bottomNavigation.inflateMenu(R.menu.menu_bottom_nav_medic)
        }

        val navController =
            (childFragmentManager.findFragmentById(R.id.mainContainerView) as NavHostFragment).navController
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}