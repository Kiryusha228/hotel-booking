package edu.booking.hotel_booking.dto

import java.time.LocalDate

data class Guest (
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: LocalDate,
    val phoneNumber: String,
)