package com.cheezycode.mvvmtest

import android.app.Application
import com.cheezycode.mvvmtest.api.ProductsAPI
import com.cheezycode.mvvmtest.repository.ProductRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoreApplication : Application() {
//Created API interface and Repository properties here, so that whenever we want to access these properties we can do it through an instance of Application(StoreApplication)
    lateinit var productsAPI: ProductsAPI
    lateinit var productRepository: ProductRepository

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fakestoreapi.com/")
            .build()

        productsAPI = retrofit.create(ProductsAPI::class.java)
        productRepository = ProductRepository(productsAPI)
    }
}