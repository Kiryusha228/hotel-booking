package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.Booking
import edu.booking.hotel_booking.entity.BookingEntity
import edu.booking.hotel_booking.service.BookingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/bookings")
class BookingController (
    private val bookingService: BookingService
) {
    @PostMapping
    fun addBooking(@RequestBody request: Booking): ResponseEntity<BookingEntity> =
        ResponseEntity.status(HttpStatus.CREATED).body(bookingService.addBooking(request))


    @PutMapping("/{id}")
    fun updateBooking(
        @PathVariable id: UUID,
        @RequestBody request: Booking,
    ): ResponseEntity<BookingEntity> =
        ResponseEntity.ok(bookingService.updateBooking(id, request))


    @DeleteMapping("/{id}")
    fun cancelBooking(@PathVariable id: UUID): ResponseEntity<UUID> =
        ResponseEntity.ok(bookingService.cancelBooking(id))

    @GetMapping("/rooms")
    fun findFreeRooms(
        @RequestParam startDateTime: LocalDateTime,
        @RequestParam endDateTime: LocalDateTime
    ): ResponseEntity<List<UUID>> =
        ResponseEntity.ok(bookingService.findFreeRooms(startDateTime, endDateTime))
}