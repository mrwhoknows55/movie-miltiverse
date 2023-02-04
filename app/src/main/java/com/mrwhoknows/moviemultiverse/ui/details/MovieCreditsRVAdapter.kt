package com.mrwhoknows.moviemultiverse.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.mrwhoknows.moviemultiverse.R
import com.mrwhoknows.moviemultiverse.databinding.ItemCreditsListBinding
import com.mrwhoknows.moviemultiverse.model.Credits

class MovieCreditsRVAdapter :
    ListAdapter<Credits, MovieCreditsRVAdapter.CreditsVH>(Credits.itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditsVH = CreditsVH(
        ItemCreditsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CreditsVH, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CreditsVH(
        private val binding: ItemCreditsListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Credits) = binding.run {
            tvHead.text = item.name
            tvSubHead.text = item.characterOrJobName
            ivProfileImg.load(item.profileImgUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                scale(Scale.FILL)
                error(R.drawable.ic_profile_placeholder)
            }
        }
    }

}