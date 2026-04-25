package edu.booking.hotel_booking.dao

import edu.booking.hotel_booking.entity.BookingEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Repository
class BookingDao (
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    @Transactional
    fun addBooking(booking: BookingEntity): BookingEntity {
        val sqlBooking = """
                            INSERT INTO booking (id, start_booking_datetime, end_booking_datetime, room_id)
                            VALUES (:id, :start, :end, :roomId)
                        """

        val bookingParams = mapOf(
            "id" to booking.id,
            "start" to booking.startBookingDateTime,
            "end" to booking.endBookingDateTime,
            "roomId" to booking.room
        )

        jdbcTemplate.update(sqlBooking, bookingParams)

        val sqlGuest = "INSERT INTO booking_guest (booking_id, guest_id) VALUES (:bookingId, :guestId)"

        booking.guests.forEach { guestId ->
            jdbcTemplate.update(sqlGuest, mapOf("bookingId" to booking.id, "guestId" to guestId))
        }

        return booking
    }

    @Transactional
    fun updateBooking(booking: BookingEntity): BookingEntity {
        val sqlUpdate = """
                            UPDATE booking
                            SET start_booking_datetime = :start,
                                end_booking_datetime = :end,
                                room_id = :roomId
                            WHERE id = :id
                        """
        jdbcTemplate.update(sqlUpdate, mapOf(
            "id" to booking.id,
            "start" to booking.startBookingDateTime,
            "end" to booking.endBookingDateTime,
            "roomId" to booking.room
        ))

        jdbcTemplate.update(
            "DELETE FROM booking_guest WHERE booking_id = :bookingId",
            mapOf("bookingId" to booking.id)
        )

        val sqlGuest = "INSERT INTO booking_guest (booking_id, guest_id) VALUES (:bookingId, :guestId)"

        booking.guests.forEach { guestId ->
            jdbcTemplate.update(sqlGuest, mapOf("bookingId" to booking.id, "guestId" to guestId))
        }

        return booking
    }

    fun cancelBooking(bookingId: UUID): UUID {
        val sql = "DELETE FROM booking WHERE id = :id"
        jdbcTemplate.update(sql, mapOf("id" to bookingId))
        return bookingId
    }

    fun findFreeRooms(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<UUID> {
        val sql = """
                    SELECT id
                    FROM room
                    WHERE room.id NOT IN (
                                    SELECT room_id
                                    FROM booking
                                    WHERE start_booking_datetime < :end_date
                                        AND end_booking_datetime > :start_date
                                    );
                    """

        val params = mapOf(
            "start_date" to startDateTime,
            "end_date" to endDateTime
        )

        return jdbcTemplate.queryForList(sql, params, UUID::class.java)
    }

    fun existsById(id: UUID): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM booking WHERE id = :id)"

        val params = mapOf("id" to id)

        return jdbcTemplate.queryForObject(sql, params , Boolean::class.java)!!
    }

}