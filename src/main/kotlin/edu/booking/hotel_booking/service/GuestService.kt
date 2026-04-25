package edu.booking.hotel_booking.service

import edu.booking.hotel_booking.dao.GuestDao
import edu.booking.hotel_booking.dto.Guest
import edu.booking.hotel_booking.entity.GuestEntity
import edu.booking.hotel_booking.exception.GuestNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GuestService (
    private val guestDao: GuestDao,
    private val generator: IdGenerator
) {
    fun addGuest(request: Guest): GuestEntity =
        guestDao.addGuest(
            GuestEntity(
                generator.generate(),
                request.firstName,
                request.lastName,
                request.middleName,
                request.birthday,
                request.phoneNumber
            )
        )

    fun updateGuest(id: UUID, request: Guest): GuestEntity {
        if (!guestDao.existsById(id)) {
            throw GuestNotFoundException("Гость не найден!")
        }

        return guestDao.updateGuest(
            GuestEntity(
                id,
                request.firstName,
                request.lastName,
                request.middleName,
                request.birthday,
                request.phoneNumber
            )
        )
    }

}