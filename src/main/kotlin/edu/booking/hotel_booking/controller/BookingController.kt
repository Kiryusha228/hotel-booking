package edu.booking.hotel_booking.controller

import edu.booking.hotel_booking.dto.Booking
import edu.booking.hotel_booking.entity.BookingEntity
import edu.booking.hotel_booking.service.BookingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Бронирование", description = "Эндпоинты для работы с бронью")
class BookingController (
    private val bookingService: BookingService
) {
    @PostMapping
    @Operation(
        summary = "Добавить информацию о брони",
        description = "В запрос передается информация о брони, без идентефикатора. " +
                "Брони присваевается идентефикатор и возвращается в ответе вместе с остальными данными. \n" +
                "При вводе неправильного идентефикатора комнаты выбрасывается исключение RoomNotFoundException. \n" +
                "При вводе неправильного идентефикатора гостя выбрасывается исключение GuestNotFoundException. \n" +
                "При вводе неправильного интервала бронирования (Дата начала позже даты конца) " +
                "выбрасывается исключение InvalidIntervalException. \n" +
                "При попытке ввести идетефикатор комнаты которая уже забронирована на выбранный промежуток" +
                "времени выбрасывается исключение RoomAlreadyBookedException"
    )
    fun addBooking(@RequestBody request: Booking): ResponseEntity<BookingEntity> =
        ResponseEntity.status(HttpStatus.CREATED).body(bookingService.addBooking(request))


    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить информацию о брони",
        description = "В запрос передается идентефикатор и новая информация о брони. " +
                "В ответе возвращается обновленная информация о брони вместе с ее идентефикатором. \n" +
                "При вводе неправильного идентефикатора брони выбрасывается исключение BookingNotFoundException. \n" +
                "При вводе неправильного идентефикатора комнаты выбрасывается исключение RoomNotFoundException. \n" +
                "При вводе неправильного идентефикатора гостя выбрасывается исключение GuestNotFoundException. \n" +
                "При вводе неправильного интервала бронирования (Дата начала позже даты конца) " +
                "выбрасывается исключение InvalidIntervalException. \n" +
                "При попытке ввести идетефикатор комнаты которая уже забронирована на выбранный промежуток" +
                "времени выбрасывается исключение RoomAlreadyBookedException"
    )
    fun updateBooking(
        @PathVariable id: UUID,
        @RequestBody request: Booking,
    ): ResponseEntity<BookingEntity> =
        ResponseEntity.ok(bookingService.updateBooking(id, request))


    @DeleteMapping("/{id}")
    @Operation(
        summary = "Отменить бронь информацию о брони",
        description = "В запрос передается идентефикатор брони. " +
                "В случае успеха возвращается идентефикатор отмененной брони.  \n" +
                "При вводе неправильного идентефикатора брони выбрасывается исключение BookingNotFoundException."

    )
    fun cancelBooking(@PathVariable id: UUID): ResponseEntity<UUID> =
        ResponseEntity.ok(bookingService.cancelBooking(id))

    @GetMapping("/rooms")
    @Operation(
        summary = "Посмотреть свободные комнаты ",
        description = "В запрос вводятся дата начала и конца брони. " +
                "В ответе возвращается список идентефикаторов свободных на выбранную дату комнат. \n" +
                "При вводе неправильного интервала бронирования (Дата начала позже даты конца) " +
                "выбрасывается исключение InvalidIntervalException. \n"
    )
    fun findFreeRooms(
        @RequestParam startDateTime: LocalDateTime,
        @RequestParam endDateTime: LocalDateTime
    ): ResponseEntity<List<UUID>> =
        ResponseEntity.ok(bookingService.findFreeRooms(startDateTime, endDateTime))
}