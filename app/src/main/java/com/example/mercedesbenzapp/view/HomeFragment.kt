package com.example.mercedesbenzapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMapClickListener {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap

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
            mercedesViewModel.price = it.distance.toString()
            mercedesViewModel.image = it.image
            mercedesViewModel.rating = it.rating
            mercedesViewModel.distance = it.distance
            mercedesViewModel.phone = it.phone
            findNavController().navigate(R.id.action_home_to_details)


        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

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
                }
                is UIState.SUCCESS<List<RestaurantDomain>> -> {

                    it.response.forEach {
                        listVT.add(ViewType.RESTAURANT(it))
                    }

                    mercedesAdapter.updateItems(listVT)
                    mercedesViewModel.saveAllBusinessByLoc(it.response[0])
                }
                is UIState.ERROR -> {
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


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMapClickListener(this)
        enableLocation()


    }


    private fun enableLocation() {
        if (!::map.isInitialized) return

        requestLocation()


    }

    private fun requestLocation() {
        val task = fusedLocationProviderClient.lastLocation
        mercedesViewModel.isLoading.postValue(false)
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = false
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
            return
        } else {
            map.isMyLocationEnabled = true
        }

        task.addOnSuccessListener {
            it?.let {
                val coordinates = LatLng(it.latitude, it.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f), 2000, null)
                mercedesViewModel.getAllBusinessByLoc(it.latitude, it.longitude)

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            }
            else -> {}

        }
    }

    override fun onResume() {
        super.onResume()
        if (!::map.isInitialized) return
        requestLocation()

    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }


    override fun onMapClick(p0: LatLng) {

        val coordinates = LatLng(p0.latitude, p0.longitude)
        val marker = MarkerOptions().position(coordinates).title("Restaurants near here")
        map.clear()
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f), 2000, null)
        mercedesViewModel.getAllBusinessByLoc(p0.latitude, p0.longitude)

    }


}