package edu.booking.hotel_booking.dao

import edu.booking.hotel_booking.entity.RoomEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RoomDao (
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun createRoom(room: RoomEntity): RoomEntity {
        val sql = "INSERT INTO room (id, floor, number, beds) VALUES (:id, :floor, :number, :beds)"

        val params = mapOf(
            "id" to room.id,
            "floor" to room.floor,
            "number" to room.number,
            "beds" to room.beds
        )

        jdbcTemplate.update(sql, params)
        return room;
    }

    fun updateRoom(room: RoomEntity): RoomEntity {
        val sql = """
                    UPDATE room
                    SET floor = :floor,
                        number = :number,
                        beds = :beds
                    WHERE id = :id
                """

        val params = mapOf(
            "id" to room.id,
            "floor" to room.floor,
            "number" to room.number,
            "beds" to room.beds
        )

        jdbcTemplate.update(sql, params)
        return room
    }
    fun deleteRoom(roomId: UUID): UUID {
        val sql = "DELETE FROM room WHERE id = :id"

        val params = mapOf("id" to roomId)

        jdbcTemplate.update(sql, params)
        return roomId
    }

    fun existsById(id: UUID): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM room WHERE id = :id)"

        val params = mapOf("id" to id)

        return jdbcTemplate.queryForObject(sql, params , Boolean::class.java)!!
    }

}