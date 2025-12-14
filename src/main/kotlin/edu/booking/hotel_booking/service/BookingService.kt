package edu.booking.hotel_booking.service

import edu.booking.hotel_booking.dao.BookingDao
import edu.booking.hotel_booking.dao.GuestDao
import edu.booking.hotel_booking.dao.RoomDao
import edu.booking.hotel_booking.dto.Booking
import edu.booking.hotel_booking.entity.BookingEntity
import edu.booking.hotel_booking.exception.BookingNotFoundException
import edu.booking.hotel_booking.exception.GuestNotFoundException
import edu.booking.hotel_booking.exception.InvalidIntervalException
import edu.booking.hotel_booking.exception.RoomAlreadyBookedException
import edu.booking.hotel_booking.exception.RoomNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class BookingService (
    private val bookingDao: BookingDao,
    private val roomDao: RoomDao,
    private val guestDao: GuestDao
) {
    fun addBooking(request: Booking): BookingEntity {
        validateRoom(request.room)
        validateGuests(request.guests)
        validateInterval(request.startBookingDateTime, request.endBookingDateTime)
        validateFreeRoom(
            request.startBookingDateTime,
            request.endBookingDateTime,
            request.room
        )

        return bookingDao.addBooking(
            BookingEntity(
                UUID.randomUUID(),
                request.startBookingDateTime,
                request.endBookingDateTime,
                request.guests,
                request.room,
            )
        )
    }


    fun updateBooking(id: UUID, request: Booking): BookingEntity {
        validateBooking(id)
        validateRoom(request.room)
        validateGuests(request.guests)
        validateInterval(request.startBookingDateTime, request.endBookingDateTime)
        validateFreeRoom(
            request.startBookingDateTime,
            request.endBookingDateTime,
            request.room
        )

        return bookingDao.updateBooking(
            BookingEntity(
                id,
                request.startBookingDateTime,
                request.endBookingDateTime,
                request.guests,
                request.room,
            )
        )
    }


    fun cancelBooking(id: UUID): UUID {
        validateBooking(id)
        return bookingDao.cancelBooking(id)
    }


    fun findFreeRooms(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<UUID> {
        validateInterval(startDateTime, endDateTime)
        return bookingDao.findFreeRooms(startDateTime, endDateTime)
    }

    private fun validateInterval(startDateTime: LocalDateTime, endDateTime: LocalDateTime){
        if (startDateTime >= endDateTime) {
            throw InvalidIntervalException(
                "Дата начала бронирования должна быть раньше даты окончания!"
            )
        }
    }

    private fun validateRoom(roomId: UUID){
        if (!roomDao.existsById(roomId)) {
            throw RoomNotFoundException("Комната не найдена!")
        }
    }

    private fun validateGuests(guests: List<UUID>){
        guests.forEach { guestId ->
            if(!guestDao.existsById(guestId)) {
                throw GuestNotFoundException("Гость не найден!")
            }
        }
    }

    private fun validateBooking(bookingId: UUID){
        if (!bookingDao.existsById(bookingId)) {
            throw BookingNotFoundException("Бронь не найдена!")
        }
    }

    private fun validateFreeRoom(startDateTime: LocalDateTime, endDateTime: LocalDateTime, room: UUID){
        val rooms = findFreeRooms(startDateTime, endDateTime)
        if (!rooms.contains(room)) {
            throw RoomAlreadyBookedException("Комната уже забронирована!")
        }
    }


}