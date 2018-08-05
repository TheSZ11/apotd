package com.szaidi.apotd.data.models

data class ApiErrorResponse(
		val error: Error
)

data class Error(
		val code: String,
		val message: String
)