--liquibase formatted sql

--changeset trainee:fill
INSERT INTO room (id, floor, number, beds) VALUES
('11111111-1111-1111-1111-111111111111', 1, 101, 2),
('22222222-2222-2222-2222-222222222222', 2, 201, 3),
('33333333-3333-3333-3333-333333333333', 3, 301, 1);

INSERT INTO guest (
    id,
    first_name,
    last_name,
    middle_name,
    birthday,
    phone_number
) VALUES
(
    'a1111111-1111-1111-1111-111111111111',
    'Иван',
    'Иванов',
    'Иванович',
    '1990-05-12',
    '89990000001'
),
(
    'a2222222-2222-2222-2222-222222222222',
    'Петр',
    'Петров',
    'Петрович',
    '1985-08-23',
    '89990000002'
),
(
    'a3333333-3333-3333-3333-333333333333',
    'Александр',
    'Александров',
    'Александрович',
    '1995-11-03',
    '89990000003'
);

INSERT INTO booking (
    id,
    start_booking_datetime,
    end_booking_datetime,
    room_id
) VALUES
(
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    '2025-01-10 14:00:00',
    '2025-01-12 12:00:00',
    '11111111-1111-1111-1111-111111111111'
),
(
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
    '2025-02-05 15:00:00',
    '2025-02-08 11:00:00',
    '22222222-2222-2222-2222-222222222222'
),
(
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    '2025-03-20 13:00:00',
    '2025-03-22 10:00:00',
    '33333333-3333-3333-3333-333333333333'
);

INSERT INTO booking_guest (booking_id, guest_id) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'a1111111-1111-1111-1111-111111111111'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'a2222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'a3333333-3333-3333-3333-333333333333');
