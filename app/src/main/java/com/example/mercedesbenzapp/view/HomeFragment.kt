package com.example.mercedesbenzapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.adapter.MercedesAdapter
import com.example.mercedesbenzapp.databinding.FragmentHomeBinding
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.utils.UIState
import com.example.mercedesbenzapp.viewmodel.MercedesViewModel

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val mercedesViewModel: MercedesViewModel by lazy {
        ViewModelProvider(requireActivity())[MercedesViewModel::class.java]
    }

    private val mercedesAdapter by lazy {
        MercedesAdapter {
            mercedesViewModel.name = it.name
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


        mercedesViewModel.getAllBusinessByLoc(33.9091094, -84.4813835)
        getAllBusiness()


        // Inflate the layout for this fragment
        return binding.root
    }


    private fun getAllBusiness() {
        mercedesViewModel.business.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.LOADING -> {
                }
                is UIState.SUCCESS<List<RestaurantDomain>> -> {
                    mercedesAdapter.updateItems(it.response ?: emptyList())
                }
                is UIState.ERROR -> {
                    it.error.localizedMessage?.let {
                        throw Exception("Error")
                    }
                }
            }
        }
    }

}