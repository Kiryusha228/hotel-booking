package edu.booking.hotel_booking.service

import edu.booking.hotel_booking.dao.RoomDao
import edu.booking.hotel_booking.dto.Room
import edu.booking.hotel_booking.entity.RoomEntity
import edu.booking.hotel_booking.exception.RoomNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoomService (
    private val roomDao: RoomDao,
) {
    fun addRoom(request: Room): RoomEntity =
        roomDao.createRoom(
            RoomEntity(
                UUID.randomUUID(),
                request.floor,
                request.number,
                request.beds
            )
        )

    fun updateRoom(id: UUID, request: Room): RoomEntity {
        validateRoom(id)
        return roomDao.updateRoom(
            RoomEntity(
                id,
                request.floor,
                request.number,
                request.beds
            )
        )
    }

    fun deleteRoom(id: UUID): UUID {
        validateRoom(id)
        return roomDao.deleteRoom(id)
    }

    private fun validateRoom(roomId: UUID){
        if (!roomDao.existsById(roomId)) {
            throw RoomNotFoundException("Комната не найдена!")
        }
    }
}