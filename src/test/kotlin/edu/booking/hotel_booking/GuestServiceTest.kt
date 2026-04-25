package edu.booking.hotel_booking

import edu.booking.hotel_booking.dao.GuestDao
import edu.booking.hotel_booking.dto.Guest
import edu.booking.hotel_booking.entity.GuestEntity
import edu.booking.hotel_booking.exception.GuestNotFoundException
import edu.booking.hotel_booking.service.GuestService
import edu.booking.hotel_booking.service.IdGenerator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals

class GuestServiceTest {
    private lateinit var guestService: GuestService

    private val guestDao = mockk<GuestDao>()
    private val generator = mockk<IdGenerator>()

    @BeforeEach
    fun setUp() {
        guestService = GuestService(guestDao, generator)
    }

    @Test
    fun createGuestTest() {
        //arrange
        val expectedId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val request = Guest(
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.now(),
            "89990009999"
        )
        val expectedEntity = GuestEntity(
            expectedId,
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.now(),
            "89990009999"
        )

        every { generator.generate() } returns expectedId
        every { guestDao.addGuest(expectedEntity) } returns expectedEntity

        //act
        val result = guestService.addGuest(request)

        //assert
        assertEquals(result, expectedEntity)
    }

    @Test
    fun updateGuestTest() {
        //arrange
        val expectedId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val request = Guest(
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.now(),
            "89990009999"
        )
        val expectedEntity = GuestEntity(
            expectedId,
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.now(),
            "89990009999"
        )

        every { guestDao.updateGuest(expectedEntity) } returns expectedEntity
        every { guestDao.existsById(expectedId) } returns true

        //act
        val result = guestService.updateGuest(expectedId, request)

        //assert
        assertEquals(result, expectedEntity)
    }

    @Test
    fun updateGuestExceptionTest() {
        //arrange
        val expectedId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val request = Guest(
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.now(),
            "89990009999"
        )
        val expectedEntity = GuestEntity(
            expectedId,
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.now(),
            "89990009999"
        )

        every { guestDao.updateGuest(expectedEntity) } returns expectedEntity
        every { guestDao.existsById(expectedId) } returns false

        //act
        val exception = assertThrows<GuestNotFoundException> {
            guestService.updateGuest(expectedId, request)
        }

        //assert
        assertEquals("Гость не найден!", exception.message)
    }
}