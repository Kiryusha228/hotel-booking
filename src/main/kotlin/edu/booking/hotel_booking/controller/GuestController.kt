package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.Guest
import edu.booking.hotel_booking.entity.GuestEntity
import edu.booking.hotel_booking.service.GuestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/guests")
class GuestController (
    private val guestService: GuestService
) {
    @PostMapping
    fun addGuest(@RequestBody request: Guest): ResponseEntity<GuestEntity> =
        ResponseEntity.status(HttpStatus.CREATED).body(guestService.addGuest(request))


    @PutMapping("/{id}")
    fun updateGuest(
        @PathVariable id: UUID,
        @RequestBody request: Guest,
    ): ResponseEntity<GuestEntity> =
        ResponseEntity.ok(guestService.updateGuest(id, request))
}