package com.example.b3networkstest

sealed class Resource<out T> {

    data class LOADING<out T>
        (val state: Boolean) : Resource<T>()

    data class SUCCESS<out T>
        (val data: T) : Resource<T>()

    data class FAILURE<out T>
        (val e: String) : Resource<T>()

    data class EMPTY<out T>
        (val e: Boolean) : Resource<T>()
}
