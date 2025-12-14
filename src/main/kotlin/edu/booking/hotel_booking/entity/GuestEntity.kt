package edu.booking.hotel_booking.entity

import java.time.LocalDate
import java.util.UUID

data class GuestEntity (
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: LocalDate,
    val phoneNumber: String,
)