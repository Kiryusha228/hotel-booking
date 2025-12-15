package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.Guest
import edu.booking.hotel_booking.entity.GuestEntity
import edu.booking.hotel_booking.kafka.producer.GuestProducer
import edu.booking.hotel_booking.service.GuestService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Гости", description = "Эндпоинты для работы с гостями")
class GuestController (
    private val guestService: GuestService,
    private val guestProducer: GuestProducer
) {
    @PostMapping
    @Operation(
        summary = "Добавить информацию о госте",
        description = "В запрос передается информация о госте, без идентефикатора. " +
                "Гостю присваевается идентефикатор и возвращается в ответе вместе с остальными данными"
    )
    fun addGuest(@RequestBody request: Guest): ResponseEntity<GuestEntity> =
        ResponseEntity.status(HttpStatus.CREATED).body(guestService.addGuest(request)).also {
            guestProducer.sendGuestInfo("Гость был добавлен!")
        }


    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить информацию о госте",
        description = "В запрос передается идентефикатор и новая информация о госте. " +
                "В ответе возвращается информация о госте с измененными данными. \n" +
                "При неправильном идентефикаторе выбрасывается исключение GuestNotFoundException"
    )
    fun updateGuest(
        @PathVariable id: UUID,
        @RequestBody request: Guest,
    ): ResponseEntity<GuestEntity> =
        ResponseEntity.ok(guestService.updateGuest(id, request)).also {
            guestProducer.sendGuestInfo("Информация о госте была изменена!")
        }
}