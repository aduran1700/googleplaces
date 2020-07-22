package com.googleplaces.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.googleplaces.data.LocationRepository
import com.googleplaces.data.model.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor (
    private val repository: LocationRepository
) : ViewModel() {

    private val results = MutableLiveData<Response>()
    private val disposable = CompositeDisposable()
    private val radiusMap = mapOf(
        0 to 1609 * 5,
        1 to 1609 * 10,
        2 to 1609 * 25,
        3 to 1609 * 100
    )
    val locationResults = results


    fun getRadius(): Int {
        return radiusMap.filter { repository.getRadius() == it.value }.keys.first()
    }

    fun saveRadius(selection: Int) {
        repository.saveRadius(radiusMap.getValue(selection))
    }

    @SuppressLint("CheckResult")
    fun getNearByLocation(location: String) {
        results.postValue(Response.Loading)
        val locations  = repository.getLocations(location, repository.getRadius())
            .subscribeOn(Schedulers.io())
            .map {
                if (it.results.size > 10) {
                    it.results.subList(0, 10)
                } else {
                    it.results
                }
            }.flatMap {
                repository.getSavedLocations()
                    .map { cached ->
                        it.plus(cached).distinct()
                    }
            }
            .toObservable()
            .switchIfEmpty(
                repository.getLocations(location, (radiusMap[getRadius() + 1] ?: 0 ))
                .toObservable().map {
                        it.results
                    }
            ).flatMapSingle {
                repository.saveLocations(it)
                    .toSingle { it }
            }
            .observeOn(
            AndroidSchedulers.mainThread())
            .subscribe(
                {
                    results.postValue(Response.ResultSuccess(it))
                },
                {
                    results.postValue(Response.ResultFailure(it.localizedMessage ?: ""))
                }
            )

        disposable.add(locations)
    }
}