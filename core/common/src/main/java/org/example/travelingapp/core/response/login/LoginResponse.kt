package org.example.travelingapp.core.response.login

import org.example.travelingapp.core.response.FrameworkResponse
import org.example.travelingapp.core.response.ValidationError
import org.example.travelingapp.core.response.login.dtos.LoginDto

class LoginResponse(
    data: LoginDto? = null,
    count: Int = 0,
    errors: List<ValidationError> = emptyList()
) : FrameworkResponse<LoginDto>(data = data, count = count, errors = errors)
