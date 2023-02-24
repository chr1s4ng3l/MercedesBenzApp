package com.example.mercedesbenzapp.rest

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*

internal class MercedesRepositoryImplTest{
    private lateinit var testObject: MercedesRepository
    private val mockServiceApi = mockk<YelpApi>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)


}