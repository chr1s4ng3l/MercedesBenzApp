package com.example.mercedesbenzapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.adapter.MercedesAdapter
import com.example.mercedesbenzapp.databinding.FragmentDetailsBinding
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.model.domain.UserDomain
import com.example.mercedesbenzapp.utils.UIState
import com.example.mercedesbenzapp.utils.ViewType
import com.example.mercedesbenzapp.viewmodel.MercedesViewModel

class DetailsFragment : Fragment() {

    private val mercedesAdapter by lazy {
        MercedesAdapter {}
    }

    private val mercedesViewModel by lazy {
        ViewModelProvider(requireActivity())[MercedesViewModel::class.java]
    }

    private val binging: FragmentDetailsBinding by lazy {
        FragmentDetailsBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binging.rvDetails.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mercedesAdapter

        }

        mercedesViewModel.getUsersReviews(mercedesViewModel.id)
        getDetails()
        getAllUsersReviews()


        // Inflate the layout for this fragment
        return binging.root
    }

    private fun getDetails() {
        binging.tvName.text = mercedesViewModel.name
        binging.tvPhone.text = mercedesViewModel.phone
        val miles = mercedesViewModel.price.toFloat() * 0.000621
        binging.tvPrice.text = "Distance: ${miles.toString().substring(0, 4)} miles"
        binging.ratingBar.rating = mercedesViewModel.rating.toFloat()

        Glide
            .with(binging.root)
            .load(mercedesViewModel.image)
            .centerCrop()
            .placeholder(R.color.gray)
            .error(R.color.gray)
            .into(binging.imageDetails)
    }


    private fun getAllUsersReviews() {
        mercedesViewModel.users.observe(viewLifecycleOwner) { state ->
            val listVT: MutableList<ViewType> = mutableListOf()
            when (state) {
                is UIState.LOADING -> {
                }
                is UIState.SUCCESS<List<UserDomain>> -> {
                    state.response.forEach {
                        listVT.add(ViewType.USER(it))

                    }
                    mercedesAdapter.updateItems(listVT)
                }
                is UIState.ERROR -> {
                    state.error.localizedMessage?.let {
                        throw Exception("Error")
                    }
                }
            }
        }
    }


}