package com.example.mercedesbenzapp.rest

import com.example.mercedesbenzapp.model.YelpResponse
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.utils.UIState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class RestaurantsRepositoryImplTest {

    private lateinit var testObject: MercedesRepository
    private val mockServiceApi = mockk<YelpApi>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)


    @Before
    fun setUp() {

        Dispatchers.setMain(testDispatcher)
        testObject = MercedesRepositoryImpl(mockServiceApi)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get all the restaurants by location when response is SUCCESS`() {
        //AAA
        //Arrange, Act, Assert.


        //Given
        coEvery {
            mockServiceApi.getHotNewRestaurants(
                latitude = 0.0,
                longitude = 0.0,
                term = "restaurants",
                attributes = "hot_and_new",
                sort = "best_match",
                limit = 20
            )
            //When
        } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns YelpResponse(
                businesses = listOf(mockk(), mockk()), total=20
            )
        }

        var state: UIState<List<RestaurantDomain>> = UIState.LOADING
        val job = testScope.launch {
            testObject.getRestaurants(lat = 0.0, long = 0.0).collect {
                state = it
            }
        }

        //Then
        assertEquals((state as UIState.SUCCESS).response, true)


        job.cancel()

    }

    }