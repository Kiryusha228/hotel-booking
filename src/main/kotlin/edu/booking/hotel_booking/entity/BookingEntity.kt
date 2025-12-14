package edu.booking.hotel_booking.entity

import java.time.LocalDateTime
import java.util.UUID

data class BookingEntity(
    val id: UUID,
    val startBookingDateTime: LocalDateTime,
    val endBookingDateTime: LocalDateTime,
    val guests: List<UUID>,
    val room: UUID
)
