package com.example.mercedesbenzapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.databinding.RestaurantItemBinding
import com.example.mercedesbenzapp.databinding.UserItemBinding
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.model.domain.UserDomain
import com.example.mercedesbenzapp.utils.ViewType

class MercedesAdapter(
    private val itemSet: MutableList<ViewType> = mutableListOf(),
    private val onClickItem: (RestaurantDomain) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun updateItems(newItems: List<ViewType>) {
        if (itemSet != newItems) {
            itemSet.clear()
            itemSet.addAll(newItems)
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            MercedesViewHolder(
                RestaurantItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            UserViewHolder(
                UserItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (itemSet[position]) {
            is ViewType.RESTAURANT -> 0
            is ViewType.USER -> 1
        }

    override fun getItemCount(): Int = itemSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemSet[position]) {
            is ViewType.RESTAURANT -> (holder as MercedesViewHolder).bind(
                item.restaurantList,
                onClickItem
            )
            is ViewType.USER -> (holder as UserViewHolder).bindUser(
                item.userList,
            )
        }
    }
}


class MercedesViewHolder(private val binding: RestaurantItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val roundingRadius = 20

    //Binding the view in the cardView
    @SuppressLint("SetTextI18n")
    fun bind(item: RestaurantDomain, onItemClick: (RestaurantDomain) -> Unit) {

        binding.tvName.text = item.name
        binding.tvPhone.text = item.phone
        binding.tvAddress.text = item.rating.toString()
        val miles = item.distance * 0.000621
        binding.tvDistance.text = "${miles.toString().substring(0, 4)} mi"

        binding.ratingBar.rating = item.rating.toFloat()

        Glide
            .with(binding.root)
            .load(item.image)
            .transform(CenterCrop(), RoundedCorners(roundingRadius))
            .placeholder(R.color.gray)
            .error(R.color.gray)
            .into(binding.imageViewRestaurant)
        itemView.setOnClickListener {
            onItemClick(item)
        }


    }

}


class UserViewHolder(private val binding: UserItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val roundingRadius = 70

    //Binding the view in the cardView
    @SuppressLint("SetTextI18n")
    fun bindUser(item: UserDomain) {

        binding.tvUserName.text = item.user.name.toString()
        binding.tvDate.text = item.date
        binding.tvReview.text = item.review
        binding.ratingBar.rating = item.rating.toFloat()

        Glide
            .with(binding.root)
            .load(item.user.imageUrl)
            .transform(CenterCrop(), RoundedCorners(roundingRadius))
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(binding.imageUser)

        itemView.setOnClickListener {

        }


    }

}