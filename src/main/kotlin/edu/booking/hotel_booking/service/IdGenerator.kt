package edu.booking.hotel_booking.service

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdGenerator {
    fun generate(): UUID = UUID.randomUUID()
}