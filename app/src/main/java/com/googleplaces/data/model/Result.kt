package com.googleplaces.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.googleplaces.BuildConfig

@Entity
data class Result(
    @PrimaryKey
    val id: String,
    val name: String,
    val photos: List<Photo>?,
    val rating: Double?
) {
    fun photo() = if (photos.isNullOrEmpty())  "" else "https://maps.googleapis.com/maps/api/place/photo?" +
            "maxwidth=100&photoreference=${photos[0].photo_reference}&key=${BuildConfig.API_KEY}"
}