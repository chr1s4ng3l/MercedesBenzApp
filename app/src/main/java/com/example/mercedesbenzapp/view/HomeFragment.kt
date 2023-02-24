package com.example.mercedesbenzapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.audiofx.Equalizer.Settings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.green
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.adapter.MercedesAdapter
import com.example.mercedesbenzapp.databinding.FragmentHomeBinding
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.utils.UIState
import com.example.mercedesbenzapp.utils.ViewType
import com.example.mercedesbenzapp.viewmodel.MercedesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {

    private lateinit var map: GoogleMap
    private val coordinates = LatLng(33.9091, -84.4792)


    companion object {
        const val REQUEST_LOCATION = 0
    }

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val mercedesViewModel: MercedesViewModel by lazy {
        ViewModelProvider(requireActivity())[MercedesViewModel::class.java]
    }

    private val mercedesAdapter by lazy {
        MercedesAdapter {
            mercedesViewModel.name = it.name
            mercedesViewModel.id = it.id
            mercedesViewModel.image = it.image
            mercedesViewModel.price = it.price
            mercedesViewModel.rating = it.rating
            mercedesViewModel.distance = it.distance
            mercedesViewModel.phone = it.phone
            findNavController().navigate(R.id.action_home_to_details)


        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding.rvHome.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter = mercedesAdapter


        }


        createFragment()

        //mercedesViewModel.getAllBusinessByLoc(mercedesViewModel.lat, mercedesViewModel.lon)
        getAllBusiness()
        progressBar()


        // Inflate the layout for this fragment
        return binding.root
    }


    private fun progressBar() {
        mercedesViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }
    }


    private fun getAllBusiness() {
        mercedesViewModel.business.observe(viewLifecycleOwner) {
            val listVT: MutableList<ViewType> = mutableListOf()
            when (it) {
                is UIState.LOADING -> {
                    mercedesViewModel.isLoading.postValue(true)
                }
                is UIState.SUCCESS<List<RestaurantDomain>> -> {
                    mercedesViewModel.isLoading.postValue(false)

                    it.response.forEach{
                        listVT.add(ViewType.RESTAURANT(it))
                    }

                    mercedesAdapter.updateItems(listVT)
                }
                is UIState.ERROR -> {
                    mercedesViewModel.isLoading.postValue(false)
                    it.error.localizedMessage?.let {
                        throw Exception("Error")
                    }
                }
            }
        }
    }

    //Create googleMaps
    private fun createFragment() {

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMapClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()
        val marker = MarkerOptions().position(coordinates).title("Me")
        map.clear()
        map.addMarker(marker)


    }


    //Location permission
    fun isLocationPermissionGranted() = ContextCompat
        .checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::map.isInitialized) return
        //Permissions Granted? Yes
        if (isLocationPermissionGranted()) {

            map.isMyLocationEnabled = false
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f), 2000, null)
            mercedesViewModel.getAllBusinessByLoc(coordinates.latitude, coordinates.longitude)



        }
        //No permissions granted? No
        else {
            requestLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        mercedesViewModel.isLoading.postValue(false)
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            android.app.AlertDialog.Builder(requireContext())
                .setMessage("To continue, turn on device location, which uses Google's locations service.")
                .setPositiveButton("GO") { Dialog, _ ->
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("NO THANKS") { Dialog, _ ->
                    map.isMyLocationEnabled = false

                }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true

            } else {
                android.app.AlertDialog.Builder(requireContext())
                    .setMessage("To continue, turn on device location, which uses Google's locations service.")
                    .setPositiveButton("GO") { Dialog, _ ->
                        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton("NO THANKS") { Dialog, _ ->
                        map.isMyLocationEnabled = false

                    }
                    .create().show()

            }

            else -> {}

        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (!isLocationPermissionGranted()) {
            if (!::map.isInitialized) return
            map.isMyLocationEnabled = false

            android.app.AlertDialog.Builder(requireContext())
                .setMessage("To continue, turn on device location, which uses Google's locations service.")
                .setPositiveButton("GO") { Dialog, _ ->
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("NO THANKS") { Dialog, _ ->
                    map.isMyLocationEnabled = false

                }
                .create().show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        val coordinates = LatLng(p0.latitude, p0.longitude)
        val marker = MarkerOptions().position(coordinates).title("Me")
        map.clear()
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 20f), 2000, null)

        mercedesViewModel.getAllBusinessByLoc(p0.latitude, p0.longitude)

    }

    override fun onMapClick(p0: LatLng) {

        val coordinates = LatLng(p0.latitude, p0.longitude)
        val marker = MarkerOptions().position(coordinates).title("Me")
        map.clear()
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f), 2000, null)

        mercedesViewModel.getAllBusinessByLoc(p0.latitude, p0.longitude)

    }


}