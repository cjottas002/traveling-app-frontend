package org.example.travelingapp.core.response.login.dtos

import org.example.travelingapp.core.response.ResponseDto

data class LoginDto(val userId: String, val token: String) : ResponseDto()