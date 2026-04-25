--liquibase formatted sql

--changeset trainee:init
CREATE TABLE IF NOT EXISTS room
(
    id     UUID PRIMARY KEY,
    floor  INT NOT NULL,
    number INT NOT NULL,
    beds   INT NOT NULL
);

CREATE TABLE IF NOT EXISTS guest
(
    id           UUID PRIMARY KEY,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    middle_name  VARCHAR(100) NOT NULL,
    birthday     DATE NOT NULL,
    phone_number VARCHAR(11) NOT NULL
);

CREATE TABLE IF NOT EXISTS booking
(
    id                     UUID PRIMARY KEY,
    start_booking_datetime TIMESTAMP NOT NULL,
    end_booking_datetime   TIMESTAMP NOT NULL,
    room_id                UUID NOT NULL,

    CONSTRAINT fk_booking_room
        FOREIGN KEY (room_id)
            REFERENCES room (id),

    CONSTRAINT chk_booking_dates
        CHECK (end_booking_datetime > start_booking_datetime)
);

CREATE TABLE IF NOT EXISTS booking_guest
(
    booking_id UUID NOT NULL,
    guest_id   UUID NOT NULL,

    CONSTRAINT pk_booking_guest
        PRIMARY KEY (booking_id, guest_id),

    CONSTRAINT fk_booking_guest_booking
        FOREIGN KEY (booking_id)
            REFERENCES booking (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_booking_guest_guest
        FOREIGN KEY (guest_id)
            REFERENCES guest (id)
            ON DELETE CASCADE
);
