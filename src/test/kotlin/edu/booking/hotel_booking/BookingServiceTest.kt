package edu.booking.hotel_booking
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
import edu.booking.hotel_booking.service.BookingService
import edu.booking.hotel_booking.service.IdGenerator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class BookingServiceTest {
    private lateinit var bookingService: BookingService

    private val bookingDao = mockk<BookingDao>()
    private val guestDao = mockk<GuestDao>()
    private val roomDao = mockk<RoomDao>()
    private val generator = mockk<IdGenerator>()

    @BeforeEach
    fun setUp() {
        bookingService = BookingService(bookingDao, roomDao, guestDao, generator)
    }

    @Test
    fun addBookingExceptionRoomTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")

        val request = Booking(
            LocalDateTime.of(2025,12, 1,0,0),
            LocalDateTime.of(2025,12, 2,0,0),
            listOf(UUID.fromString("21111111-1111-1111-1111-111111111111"),
                UUID.fromString("31111111-1111-1111-1111-111111111111")),
            roomId
        )

        every { roomDao.existsById(roomId) } returns false


        //act
        val exception = assertThrows<RoomNotFoundException> {
            bookingService.addBooking(request)
        }

        //assert
        assertEquals("Комната не найдена!", exception.message)
    }

    @Test
    fun addBookingExceptionGuestTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")

        val request = Booking(
            LocalDateTime.of(2025,12, 1,0,0),
            LocalDateTime.of(2025,12, 2,0,0),
            listOf(guestId),
            roomId
        )

        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns false


        //act
        val exception = assertThrows<GuestNotFoundException> {
            bookingService.addBooking(request)
        }

        //assert
        assertEquals("Гость не найден!", exception.message)
    }

    @Test
    fun addBookingExceptionIntervalTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val start = LocalDateTime.of(2025,12, 2,0,0)
        val end = LocalDateTime.of(2025,12, 1,0,0)

        val request = Booking(
            start,
            end,
            listOf(guestId),
            roomId
        )

        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns true

        //act
        val exception = assertThrows<InvalidIntervalException> {
            bookingService.addBooking(request)
        }

        //assert
        assertEquals("Дата начала бронирования должна быть раньше даты окончания!", exception.message)
    }

    @Test
    fun addBookingExceptionFreeRoomTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val start = LocalDateTime.of(2025,12, 1,0,0)
        val end = LocalDateTime.of(2025,12, 2,0,0)

        val request = Booking(
            start,
            end,
            listOf(guestId),
            roomId
        )

        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns true
        every { bookingDao.findFreeRooms(start,end)} returns emptyList()

        //act
        val exception = assertThrows<RoomAlreadyBookedException> {
            bookingService.addBooking(request)
        }

        //assert
        assertEquals("Комната уже забронирована!", exception.message)
    }

    @Test
    fun addBookingTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("31111111-1111-1111-1111-111111111111")
        val start = LocalDateTime.of(2025,12, 1,0,0)
        val end = LocalDateTime.of(2025,12, 2,0,0)

        val request = Booking(
            start,
            end,
            listOf(guestId),
            roomId
        )

        val expectedEntity = BookingEntity(
            bookingId,
            start,
            end,
            listOf(guestId),
            roomId
        )

        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns true
        every { bookingDao.findFreeRooms(start,end)} returns listOf(roomId)
        every { bookingDao.addBooking(expectedEntity)} returns expectedEntity
        every { generator.generate() } returns bookingId

        //act
        val result = bookingService.addBooking(request)


        //assert
        assertEquals(result, expectedEntity)
    }

    //------------------------update

    @Test
    fun updateBookingExceptionBookingTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")

        val request = Booking(
            LocalDateTime.of(2025,12, 1,0,0),
            LocalDateTime.of(2025,12, 2,0,0),
            listOf(UUID.fromString("21111111-1111-1111-1111-111111111111")),
            roomId
        )


        every {bookingDao.existsById(bookingId)} returns false


        //act
        val exception = assertThrows<BookingNotFoundException> {
            bookingService.updateBooking(bookingId, request)
        }

        //assert
        assertEquals("Бронь не найдена!", exception.message)
    }

    @Test
    fun updateBookingExceptionRoomTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")

        val request = Booking(
            LocalDateTime.of(2025,12, 1,0,0),
            LocalDateTime.of(2025,12, 2,0,0),
            listOf(UUID.fromString("21111111-1111-1111-1111-111111111111")),
            roomId
        )

        every {bookingDao.existsById(bookingId)} returns true
        every { roomDao.existsById(roomId) } returns false


        //act
        val exception = assertThrows<RoomNotFoundException> {
            bookingService.updateBooking(bookingId, request)
        }

        //assert
        assertEquals("Комната не найдена!", exception.message)
    }

    @Test
    fun updateBookingExceptionGuestTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")

        val request = Booking(
            LocalDateTime.of(2025,12, 1,0,0),
            LocalDateTime.of(2025,12, 2,0,0),
            listOf(guestId),
            roomId
        )

        every {bookingDao.existsById(bookingId)} returns true
        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns false


        //act
        val exception = assertThrows<GuestNotFoundException> {
            bookingService.updateBooking(bookingId, request)
        }

        //assert
        assertEquals("Гость не найден!", exception.message)
    }

    @Test
    fun updateBookingExceptionIntervalTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")
        val start = LocalDateTime.of(2025,12, 2,0,0)
        val end = LocalDateTime.of(2025,12, 1,0,0)

        val request = Booking(
            start,
            end,
            listOf(guestId),
            roomId
        )

        every {bookingDao.existsById(bookingId)} returns true
        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns true

        //act
        val exception = assertThrows<InvalidIntervalException> {
            bookingService.updateBooking(bookingId, request)
        }

        //assert
        assertEquals("Дата начала бронирования должна быть раньше даты окончания!", exception.message)
    }

    @Test
    fun updateBookingExceptionFreeRoomTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")
        val start = LocalDateTime.of(2025,12, 1,0,0)
        val end = LocalDateTime.of(2025,12, 2,0,0)

        val request = Booking(
            start,
            end,
            listOf(guestId),
            roomId
        )

        every {bookingDao.existsById(bookingId)} returns true
        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns true
        every { bookingDao.findFreeRooms(start,end)} returns emptyList()

        //act
        val exception = assertThrows<RoomAlreadyBookedException> {
            bookingService.updateBooking(bookingId, request)
        }

        //assert
        assertEquals("Комната уже забронирована!", exception.message)
    }

    @Test
    fun updateBookingTest() {
        //arrange
        val roomId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val guestId = UUID.fromString("21111111-1111-1111-1111-111111111111")
        val bookingId = UUID.fromString("31111111-1111-1111-1111-111111111111")
        val start = LocalDateTime.of(2025,12, 1,0,0)
        val end = LocalDateTime.of(2025,12, 2,0,0)

        val request = Booking(
            start,
            end,
            listOf(guestId),
            roomId
        )

        val expectedEntity = BookingEntity(
            bookingId,
            start,
            end,
            listOf(guestId),
            roomId
        )

        every {bookingDao.existsById(bookingId)} returns true
        every { roomDao.existsById(roomId) } returns true
        every { guestDao.existsById(guestId) } returns true
        every { bookingDao.findFreeRooms(start,end)} returns listOf(roomId)
        every { bookingDao.updateBooking(expectedEntity)} returns expectedEntity
        every { generator.generate() } returns bookingId

        //act
        val result = bookingService.updateBooking(bookingId, request)

        //assert
        assertEquals(result, expectedEntity)
    }

    @Test
    fun cancelBookingExceptionTest() {
        //arrange
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")

        every {bookingDao.existsById(bookingId)} returns false

        //act
        val exception = assertThrows<BookingNotFoundException> {
            bookingService.cancelBooking(bookingId)
        }

        //assert
        assertEquals("Бронь не найдена!", exception.message)
    }

    @Test
    fun cancelBookingTest() {
        //arrange
        val bookingId = UUID.fromString("41111111-1111-1111-1111-111111111111")

        every {bookingDao.existsById(bookingId)} returns true
        every { bookingDao.cancelBooking(bookingId) } returns bookingId

        //act
        val result = bookingService.cancelBooking(bookingId)


        //assert
        assertEquals(result, bookingId)
    }


}