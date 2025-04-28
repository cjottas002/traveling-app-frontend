package org.example.travelingapp.core.response

import org.example.travelingapp.core.response.interfaces.IFrameworkResponse

open class FrameworkResponse<TResponseDto : ResponseDto>(
    override var data: TResponseDto? = null,
    override val count: Int = 0,
    val errors: List<ValidationError> = emptyList(),
    val success: Boolean = errors.isEmpty()
) : IFrameworkResponse<TResponseDto>

open class FrameworkListResponse<TResponseDto : ResponseDto>(
    override var data: List<TResponseDto> = emptyList(),
    override val count: Int = 0,
    val errors: List<ValidationError> = emptyList(),
    val success: Boolean = errors.isEmpty()
) : IFrameworkResponse<List<TResponseDto>>