package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.Room
import edu.booking.hotel_booking.entity.RoomEntity
import edu.booking.hotel_booking.kafka.producer.RoomProducer
import edu.booking.hotel_booking.service.RoomService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Комнаны", description = "Эндпоинты для работы с комнатами")
class RoomController (
    private val roomService: RoomService,
    private val roomProducer: RoomProducer
) {

    @PostMapping
    @Operation(
        summary = "Добавить информацию о комнате",
        description = "В запрос передается информация о комнате, без идентефикатора. " +
                "Комнате присваевается идентефикатор и возвращается в ответе вместе с остальными данными"
    )
    fun addRoom(@RequestBody request: Room): ResponseEntity<RoomEntity> =
        ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(request)).also {
            roomProducer.sendRoomInfo("Добавлена новая комната!")
        }


    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить информацию о комнате",
        description = "В запрос передается идентефикатор и новая информация о комнате. " +
                "В ответе возвращается информация о комнате с измененными данными. \n" +
                "При неправильном идентефикаторе выбрасывается исключение RoomNotFoundException"
    )
    fun updateRoom(
        @PathVariable id: UUID,
        @RequestBody request: Room,
    ): ResponseEntity<RoomEntity> =
        ResponseEntity.ok(roomService.updateRoom(id, request)).also {
            roomProducer.sendRoomInfo("Информация о комнате была обновлена!")
        }


    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить информацию о комнате",
        description = "В запрос передается идентефикатор" +
                "В случае успеха возвращается идентефикатор удаленной комнаты. \n" +
                "При неправильном идентефикаторе выбрасывается исключение RoomNotFoundException"
    )
    fun deleteRoom(@PathVariable id: UUID): ResponseEntity<UUID> =
        ResponseEntity.ok(roomService.deleteRoom(id)).also {
            roomProducer.sendRoomInfo("Информация о комнате была удалена!")
        }
}