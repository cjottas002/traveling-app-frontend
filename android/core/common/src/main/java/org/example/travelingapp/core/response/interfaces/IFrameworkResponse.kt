package org.example.travelingapp.core.response.interfaces

interface IFrameworkResponse<T> {
    val data: T?
    val count: Int
}