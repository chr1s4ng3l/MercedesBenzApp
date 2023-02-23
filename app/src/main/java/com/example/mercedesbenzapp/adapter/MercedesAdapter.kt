package com.example.mercedesbenzapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mercedesbenzapp.R
import com.example.mercedesbenzapp.databinding.RestaurantItemBinding
import com.example.mercedesbenzapp.model.domain.RestaurantDomain

class MercedesAdapter(
    private val itemSet: MutableList<RestaurantDomain> = mutableListOf(),
    private val onClickItem: (RestaurantDomain) -> Unit
) : RecyclerView.Adapter<MercedesViewHolder>() {


    fun updateItems(newItems: List<RestaurantDomain>) {
        if (itemSet != newItems) {
            itemSet.clear()
            itemSet.addAll(newItems)

            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MercedesViewHolder =
        MercedesViewHolder(
            RestaurantItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false

            )
        )


    override fun getItemCount(): Int = itemSet.size

    override fun onBindViewHolder(holder: MercedesViewHolder, position: Int) =
        holder.bind(itemSet[position], onClickItem)
}


class MercedesViewHolder(private val binding: RestaurantItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val roundingRadius = 10

    //Binding the view in the cardView
    fun bind(item: RestaurantDomain, onItemClick: (RestaurantDomain) -> Unit) {

        binding.tvName.text = item.name
        binding.tvPhone.text = item.phone

        binding.ratingBar.rating = item.rating.toFloat()

        Glide
            .with(binding.root)
            .load(item.image)
            .transform(CenterCrop(), RoundedCorners(roundingRadius))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imageViewRestaurant)
        itemView.setOnClickListener {
            onItemClick(item)
        }


    }

}