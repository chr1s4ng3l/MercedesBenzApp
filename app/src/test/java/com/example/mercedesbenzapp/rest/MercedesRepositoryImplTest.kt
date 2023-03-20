package com.example.mercedesbenzapp.rest

import com.example.mercedesbenzapp.model.UserResponse
import com.example.mercedesbenzapp.model.YelpResponse
import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.model.domain.UserDomain
import com.example.mercedesbenzapp.utils.UIState
import io.mockk.*
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

@OptIn(ExperimentalCoroutinesApi::class)
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


    @Test
    fun `Get all the restaurants by location when server has a list of items and return a SUCCES state`() {
        //AAA
        //Arrange, Act, Assert.


        //Given
        coEvery {
            mockServiceApi.getHotNewRestaurants(
                0.0, 0.0
            )
            //When
        } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns YelpResponse(
                listOf(
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    mockk(relaxed = true)
                )
            )
        }

        val state = mutableListOf<UIState<List<RestaurantDomain>>>()
        val job = testScope.launch {
            testObject.getRestaurants(0.0, 0.0).collect {
                state.add(it)

            }
        }

        //Then
        assertEquals(2, state.size)
        val success = (state[1] as UIState.SUCCESS).response
        assertEquals(3, success.size)

        coVerify { mockServiceApi.getHotNewRestaurants(0.0, 0.0) }
        job.cancel()

    }

//
//    @Test
//    fun `Get all the UserReviews by ID when server has a list of items and return a SUCCES`() {
//        //AAA
//        //Arrange, Act, Assert.
//
//
//        //Given
//        coEvery {
//            mockServiceApi.getUsersReview(
//                "123"
//            )
//            //When
//        } returns mockk {
//            every { isSuccessful } returns true
//            every { body() } returns UserResponse(
//                listOf(
//                    mockk(relaxed = true),
//                    mockk(relaxed = true)
//
//                ), total = 2
//            )
//        }
//
//        val state = mutableListOf<UIState<List<UserDomain>>>()
//        val job = testScope.launch {
//            testObject.getUsers("123").collect {
//                state.add(it)
//
//            }
//        }
//
//        //Then
//        assertEquals(2, state.size)
//        val success = (state[1] as UIState.SUCCESS).response
//        assertEquals(3, success.size)
//        job.cancel()
//
//    }


}