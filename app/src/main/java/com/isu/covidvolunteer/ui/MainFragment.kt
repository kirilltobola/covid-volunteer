package com.isu.covidvolunteer.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.role.RoleDto
import com.isu.covidvolunteer.service.ChatService
import com.isu.covidvolunteer.service.OrderService
import com.isu.covidvolunteer.util.UserDetails

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        const val MEDIC_ROLE = "ROLE_MEDIC"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if(UserDetails.hasRole(RoleDto(MEDIC_ROLE))) {
            bottomNavigation.menu.clear()
            bottomNavigation.inflateMenu(R.menu.menu_bottom_nav_medic)
        }

        val navController =
            (childFragmentManager.findFragmentById(R.id.mainContainerView) as NavHostFragment).navController
        NavigationUI.setupWithNavController(bottomNavigation, navController)

        startChatService()
        startOrderService()
    }

    private fun startChatService() {
        Intent(requireContext(), ChatService::class.java).also {
            requireActivity().startService(it)
        }
    }

    private fun startOrderService() {
        Intent(requireContext(), OrderService::class.java).also {
            requireActivity().startService(it)
        }
    }
}