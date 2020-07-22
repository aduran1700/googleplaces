package com.googleplaces.data

import android.content.SharedPreferences
import com.googleplaces.client.api.NearByLocationApi
import com.googleplaces.data.model.Result
import io.reactivex.schedulers.Schedulers

class LocationRepository(
    private val api: NearByLocationApi,
    private val dao: ResultDao,
    private val sharedPreferences: SharedPreferences
) {

    fun getLocations(location: String, radius: Int) =
        api.getLocations(location, radius)

    fun saveLocations(locations: List<Result>) =
        dao.insertAll(locations).subscribeOn(Schedulers.io())

    fun getSavedLocations() =
        dao.getAll().subscribeOn(Schedulers.io()).onErrorReturn {
            emptyList()
        }

    fun saveRadius(radius: Int) {
        sharedPreferences.edit().putInt(RADIUS, radius).apply()
    }

    fun getRadius(): Int = sharedPreferences.getInt(RADIUS, 16090)



    companion object {
        const val RADIUS = "RADIUS"
    }
}