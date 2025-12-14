package edu.booking.hotel_booking.dao

import edu.booking.hotel_booking.entity.GuestEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class GuestDao (
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun addGuest(guest: GuestEntity): GuestEntity {
        val sql = """
                    INSERT INTO guest (id, first_name, last_name, middle_name, birthday, phone_number)
                    VALUES(:id, :firstName, :lastName, :middleName, :birthday, :phoneNumber)
                    """

        val params = mapOf(
            "id" to guest.id,
            "firstName" to guest.firstName,
            "lastName" to guest.lastName,
            "middleName" to guest.middleName,
            "birthday" to guest.birthday,
            "phoneNumber" to guest.phoneNumber
        )

        jdbcTemplate.update(sql, params)

        return guest
    }

    fun updateGuest(guest: GuestEntity): GuestEntity {
        val sql = """
                    UPDATE guest
                    SET first_name = :firstName,
                        last_name = :lastName,
                        middle_name = :middleName,
                        birthday = :birthday,
                        phone_number = :phoneNumber
                    WHERE id = :id
                    """

        val params = mapOf(
            "id" to guest.id,
            "firstName" to guest.firstName,
            "lastName" to guest.lastName,
            "middleName" to guest.middleName,
            "birthday" to guest.birthday,
            "phoneNumber" to guest.phoneNumber
        )

        jdbcTemplate.update(sql, params)

        return guest
    }

    fun existsById(id: UUID): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM guest WHERE id = :id)"

        val params = mapOf("id" to id)

        return jdbcTemplate.queryForObject(sql, params , Boolean::class.java)!!
    }

}