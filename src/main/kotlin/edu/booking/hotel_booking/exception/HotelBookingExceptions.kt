package edu.booking.hotel_booking.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidIntervalException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class RoomNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RoomAlreadyBookedException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class GuestNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookingNotFoundException(message: String) : RuntimeException(message)