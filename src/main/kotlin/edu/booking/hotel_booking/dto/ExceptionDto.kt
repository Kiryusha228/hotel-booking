package edu.booking.hotel_booking.dto

data class ExceptionDto(
    val message: String,
    val info: MutableMap<String, Any>
)
