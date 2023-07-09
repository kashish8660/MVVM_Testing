package com.cheezycode.mvvmtest.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cheezycode.mvvmtest.getOrAwaitValue
import com.cheezycode.mvvmtest.repository.ProductRepository
import com.cheezycode.mvvmtest.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() //Used to test Arch componenets like ViewModel and LiveData

    @Mock
    lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher) //As by default ViewModelScope runs on Main Thread if dispatcher is not explicitly mentioned.
    }

    @Test
    fun test_GetProducts() = runTest{ //using runTest as repository.getProducts() is a suspend function
        Mockito.`when`(repository.getProducts()).thenReturn(NetworkResult.Success(emptyList())) //whenever you want to test an output which returns a list of
        //of any type(like of ProductListItem type), then use emptyList() function
        val sut = MainViewModel(repository)
        sut.getProducts() //it runs a coroutine
        testDispatcher.scheduler.advanceUntilIdle() //it'll make sure that LiveData on "products" is pkka set through sut.getProducts()
        val result = sut.products.getOrAwaitValue() //getOrAwaitValue() is used for LiveData
        Assert.assertEquals(0, result.data!!.size)
    }

    @Test
    fun test_GetProduct_expectedError() = runTest{
        Mockito.`when`(repository.getProducts()).thenReturn(NetworkResult.Error("Something Went Wrong"))

        val sut = MainViewModel(repository)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()
        Assert.assertEquals(true, result is NetworkResult.Error) //"is" keyword is used to check "type" of the object and not it's value
        Assert.assertEquals("Something Went Wrong", result.message)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}












