package edu.booking.hotel_booking.dto

import java.time.LocalDateTime
import java.util.UUID

data class Booking(
    val startBookingDateTime: LocalDateTime,
    val endBookingDateTime: LocalDateTime,
    val guests: List<UUID>,
    val room: UUID
)