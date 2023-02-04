package com.mrwhoknows.moviemultiverse.model

import androidx.recyclerview.widget.DiffUtil.ItemCallback

data class Credits(
    val id: Long,
    val name: String,
    val characterOrJobName: String,
    val profileImgUrl: String,
    val gender: Int,
    val department: String
) {
    companion object {
        val itemCallback = object : ItemCallback<Credits>() {
            override fun areItemsTheSame(oldItem: Credits, newItem: Credits): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Credits, newItem: Credits): Boolean =
                oldItem == newItem

        }
    }
}