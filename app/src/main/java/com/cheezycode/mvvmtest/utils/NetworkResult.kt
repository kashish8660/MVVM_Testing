package com.cheezycode.mvvmtest.utils

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) { //In this project "T" is List<ProductListItem>

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()

}