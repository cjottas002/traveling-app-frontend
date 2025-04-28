package org.example.travelingapp.core.request

open class FrameworkRequest(
    var pageIndex : Int = 1,
    var pageSize : Int = 10,
    var orderBy : String = "",
    var orderByAsc : Boolean = true,
)