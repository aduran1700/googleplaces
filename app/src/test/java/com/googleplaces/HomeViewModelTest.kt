package com.googleplaces

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.googleplaces.client.model.NearByPlaces
import com.googleplaces.data.LocationRepository
import com.googleplaces.data.model.Response
import com.googleplaces.data.model.Result
import com.googleplaces.ui.home.HomeViewModel
import com.googleplaces.utils.TestConstants
import com.googleplaces.utils.TestUtils
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var locationRepository: LocationRepository

    lateinit var SUT: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        TestUtils.setupRxjavaForOneThreadedTest()
        every { locationRepository.getLocations(any(), any()) } returns
                Single.just(NearByPlaces(TestConstants.locationList))

        every { locationRepository.getRadius() } returns 1609 * 10
        every { locationRepository.saveLocations(any()) } returns Completable.complete()
        SUT = HomeViewModel(locationRepository)
    }

    @Test
    fun `test getNearByLocation`() {
        every { locationRepository.getSavedLocations() } returns
                Single.just(TestConstants.locationList)

        every { locationRepository.getSavedLocations() } returns
                Single.just(TestConstants.locationList)
        SUT.locationResults.observeForever { }
        SUT.getNearByLocation("")

        val value : Response? = SUT.locationResults.value

        assertEquals(value, Response.ResultSuccess(TestConstants.locationList))
        value as Response.ResultSuccess
        assertEquals(value.results.size, 3)
    }

    @Test
    fun `test getNearByLocation with cache`() {
        every { locationRepository.getSavedLocations() } returns
                Single.just(TestConstants.locationList)

        every { locationRepository.getSavedLocations() } returns
                Single.just(TestConstants.locationList2)
        SUT.locationResults.observeForever { }
        SUT.getNearByLocation("")

        val value : Response? = SUT.locationResults.value

        assertEquals(value, Response.ResultSuccess(TestConstants.locationList2))
        value as Response.ResultSuccess
        assertEquals(value.results.size, 4)
    }

}