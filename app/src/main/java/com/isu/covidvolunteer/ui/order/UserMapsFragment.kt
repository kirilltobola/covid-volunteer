package com.isu.covidvolunteer.ui.order

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.isu.covidvolunteer.R
import java.util.*

class UserMapsFragment : Fragment(R.layout.fragment_maps) {
    private lateinit var map: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val geocoder = Geocoder(context, Locale("RU"))

        view.findViewById<EditText>(R.id.mapsFragmentEditText).isVisible = false
        view.findViewById<EditText>(R.id.mapsFragmentSubmitButton).isVisible = false

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(
            R.id.mapsFragmentMap
        ) as SupportMapFragment

        lifecycle.coroutineScope.launchWhenCreated {
            map = mapFragment.awaitMap()

            val addressLine: String? = arguments?.getString("address")
            if (!addressLine.isNullOrEmpty()) {
                val latlng = geocoder.getFromLocationName(addressLine, 1)[0]
                val location = LatLng(latlng.latitude, latlng.longitude)

                map.clear()
                map.addMarker {
                    position(location)
                }
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 17.toFloat())
                )
            }
        }
    }
}