package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.Room
import edu.booking.hotel_booking.entity.RoomEntity
import edu.booking.hotel_booking.service.RoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/rooms")
class RoomController (
    private val roomService: RoomService,
) {

    @PostMapping
    fun addRoom(@RequestBody request: Room): ResponseEntity<RoomEntity> =
        ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(request))


    @PutMapping("/{id}")
    fun updateRoom(
        @PathVariable id: UUID,
        @RequestBody request: Room,
    ): ResponseEntity<RoomEntity> =
        ResponseEntity.ok(roomService.updateRoom(id, request))


    @DeleteMapping("/{id}")
    fun deleteRoom(@PathVariable id: UUID): ResponseEntity<UUID> =
        ResponseEntity.ok(roomService.deleteRoom(id))
}