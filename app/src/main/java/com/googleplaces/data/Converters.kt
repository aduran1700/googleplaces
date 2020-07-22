package com.googleplaces.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.googleplaces.data.model.Photo
import java.util.*


class Converters {
    @TypeConverter
    fun stringToPhotoList(data: String?): List<Photo?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType =
            object : TypeToken<List<Photo?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun photoListToString(photoList: List<Photo?>?): String? {
        return Gson().toJson(photoList)
    }
}
