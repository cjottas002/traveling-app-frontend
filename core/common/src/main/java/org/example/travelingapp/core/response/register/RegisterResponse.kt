package org.example.travelingapp.core.response.register

import org.example.travelingapp.core.response.FrameworkResponse
import org.example.travelingapp.core.response.ValidationError
import org.example.travelingapp.core.response.register.dtos.RegisterDto

class RegisterResponse(
    data: RegisterDto? = null,
    count: Int = 0,
    errors: List<ValidationError> = emptyList()
) : FrameworkResponse<RegisterDto>(data = data, count = count, errors = errors)
