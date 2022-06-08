package com.isu.covidvolunteer.ui.order

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.isu.covidvolunteer.R
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*


class MapsFragment : DialogFragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermissionGranted = false
    private var map: GoogleMap? = null
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(52.2978, 104.2964)
    private var location: LatLng? = null


    private lateinit var geocoder: Geocoder

    // Views
    private lateinit var locationEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.mapsFragmentSubmitButton).setOnClickListener {
            var res: String = ""
            if (location != null) {
                res = geocoder.getFromLocation(
                    location!!.latitude,
                    location!!.longitude,
                    1
                )[0].getAddressLine(0)
            }
            setFragmentResult(
                "REQUEST_KEY",
                bundleOf("SELECTED_ADDRESS" to res)
            )
            this.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //geocoder
        geocoder = Geocoder(context, Locale("RU"))

        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        locationEditText = view.findViewById(R.id.mapsFragmentEditText)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(
            R.id.mapsFragmentMap
        ) as SupportMapFragment

        lifecycle.coroutineScope.launchWhenCreated {
            map = mapFragment.awaitMap()

            val addressLine: String? = arguments?.getString("addressLine")
            if (!addressLine.isNullOrEmpty()) {
                val latlng = geocoder.getFromLocationName(addressLine, 1)[0]
                val loc = LatLng(latlng.latitude, latlng.longitude)

                location = loc
                locationEditText.setText(addressLine)

                map?.clear()
                map?.addMarker {
                    position(loc)
                }
                map?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(loc, 17.toFloat())
                )
            } else {
                // Prompt the user for permission.
                getLocationPermission()

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI()

                // Get the current location of the device and set the position of the map.
                getDeviceLocation()
            }

            map?.setOnMapClickListener {
                location = it
                val geocoder: Geocoder = Geocoder(context, Locale("RU"))
                val geo = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val address = geo[0].getAddressLine(0)
                locationEditText.setText(address)

//                map?.clear()
//                map?.addMarker {
//                    position(it)
//                }
//                map?.moveCamera(
//                    CameraUpdateFactory.newLatLngZoom(it, 17.toFloat())
//                )
            }

            // edit text location
            //locationEditText.setText(addressLine)
            locationEditText.doAfterTextChanged {
                CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
                    val geocoder: Geocoder = Geocoder(context, Locale("RU"))
                    val addressLatLng: LatLng? = withContext(Dispatchers.IO) {
                        try {
                            val location = geocoder.getFromLocationName(it.toString(), 1)
                            if (location.isNotEmpty()) {
                                val loc = location[0]
                                LatLng(loc.latitude, loc.longitude)
                            } else {
                                null
                            }
                        } catch (e: IOException) {
                            Log.d("MAPS_EDITTEXT_TEST", "grpc failed")
                            null
                        }
                    }

                    if (addressLatLng != null) {
                        map?.clear()
                        map?.addMarker {
                            position(addressLatLng)
                        }

                        // camera pos
                        val projection: Projection = map!!.projection
                        val markerPosition = addressLatLng
                        val markerPoint = projection.toScreenLocation(markerPosition)
                        val targetPoint = Point(markerPoint.x, markerPoint.y - view.height / 4)
                        val targetPosition = projection.fromScreenLocation(targetPoint)
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(targetPosition, 16.toFloat())
                        )
                        location = addressLatLng
                    }
                }
            }
        }
        return view
    }



    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            1 -> {

                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val myLocation = LatLng(lastKnownLocation!!.latitude,
                                lastKnownLocation!!.longitude)
                            location = myLocation
                            val markerOptions = MarkerOptions()
                            markerOptions.position(myLocation)

                            map?.addMarker(markerOptions)
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                myLocation, 17.toFloat()))
                        }
                    } else {
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, 15.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}