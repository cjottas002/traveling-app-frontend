package org.example.travelingapp.core.extensions

import org.example.travelingapp.core.request.FrameworkRequest

fun FrameworkRequest.toQueryMap(): Map<String, String> = mapOf(
    "pageIndex"   to pageIndex.toString(),
    "pageSize"    to pageSize.toString(),
    "orderBy"     to orderBy,
    "orderByAsc"  to orderByAsc.toString()
)