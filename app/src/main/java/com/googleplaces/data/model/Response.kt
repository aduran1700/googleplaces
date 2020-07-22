package com.googleplaces.data.model

sealed class Response {
    data class ResultSuccess(val results: List<Result>): Response()
    data class ResultFailure(val error: String): Response()
    object Loading: Response()
}