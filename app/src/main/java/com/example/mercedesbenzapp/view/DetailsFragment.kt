package com.example.mercedesbenzapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.databinding.FragmentDetailsBinding
import com.example.mercedesbenzapp.viewmodel.MercedesViewModel

class DetailsFragment : Fragment() {


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


        getDetails()


        // Inflate the layout for this fragment
        return binging.root
    }


    private fun getDetails() {
        binging.tvName.text = mercedesViewModel.name
        binging.tvPhone.text = mercedesViewModel.phone
        binging.tvPrice.text = mercedesViewModel.price
        binging.ratingBar.rating = mercedesViewModel.rating.toFloat()

        Glide
            .with(binging.root)
            .load(mercedesViewModel.image)
            .centerCrop()
            .placeholder(R.color.gray)
            .error(R.color.gray)
            .into(binging.imageDetails)
    }


}