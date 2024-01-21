INSERT INTO customer (customer_id, created_at, is_activated, updated_at, account, account_holder, bank, email, nickname,
                      optional_agreement, password, phone_number, recovery_email, counselor_id)
VALUES (1, '2024-01-19 22:33:23', 1, '2024-01-19 22:33:23', NULL, NULL, NULL, 'user1@gmail.com', '사용자1', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0001', 'recovery1@gmail.com', NULL),
       (2, '2024-01-19 22:34:23', 1, '2024-01-19 22:34:23', NULL, NULL, NULL, 'user2@gmail.com', '사용자2', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0002', 'recovery2@gmail.com', NULL),
       (3, '2024-01-19 22:35:23', 1, '2024-01-19 22:35:23', NULL, NULL, NULL, 'user3@gmail.com', '사용자3', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0003', 'recovery3@gmail.com', NULL),
       (4, '2024-01-19 22:36:23', 1, '2024-01-19 22:36:23', NULL, NULL, NULL, 'user4@gmail.com', '사용자4', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0004', 'recovery4@gmail.com', NULL),
       (5, '2024-01-19 22:37:23', 1, '2024-01-19 22:37:23', NULL, NULL, NULL, 'user5@gmail.com', '사용자5', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0005', 'recovery5@gmail.com', NULL),
       (6, '2024-01-19 22:38:23', 1, '2024-01-19 22:38:23', NULL, NULL, NULL, 'user6@gmail.com', '사용자6', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0006', 'recovery6@gmail.com', NULL),
       (7, '2024-01-19 22:39:23', 1, '2024-01-19 22:39:23', NULL, NULL, NULL, 'user7@gmail.com', '사용자7', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0007', 'recovery7@gmail.com', NULL),
       (8, '2024-01-19 22:40:23', 1, '2024-01-19 22:40:23', NULL, NULL, NULL, 'user8@gmail.com', '사용자8', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0008', 'recovery8@gmail.com', NULL),
       (9, '2024-01-19 22:41:23', 1, '2024-01-19 22:41:23', NULL, NULL, NULL, 'user9@gmail.com', '사용자9', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0009', 'recovery9@gmail.com', NULL),
       (10, '2024-01-19 22:42:23', 1, '2024-01-19 22:42:23', NULL, NULL, NULL, 'user10@gmail.com', '사용자10', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0010', 'recovery10@gmail.com', NULL),
       (11, '2024-01-19 22:33:23', 1, '2024-01-19 22:33:23', NULL, NULL, NULL, 'admin@gmail.com', '관리자', NULL,
        '$2a$10$pThW9QspTg03s8n6Tmh.POl5HWMSDMNfGGJXWUoJs5KdecWRekkre', '010-0000-0001', 'recovery1@gmail.com', NULL);
-- customer data 10개, admin 1개 생성
INSERT INTO customer_roles (customer_customer_id, roles)
VALUES (1, 0),
       (2, 0),
       (3, 0),
       (4, 0),
       (5, 0),
       (6, 0),
       (7, 0),
       (8, 0),
       (9, 0),
       (10, 0),
       (11, 2);
-- customer role 설정
INSERT INTO counselor (counselor_id, created_at, is_activated, updated_at, account, account_holder, bank, consult_style,
                       experience, introduction, is_educated, level, nickname, profile_status, rating_average,
                       retry_education, total_review, profile_updated_at, total_consult)
VALUES (1, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'SYMPATHY', '경험적는란1 aaa', '안녕하세요1', 1, 1,
        'hello1', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0),
       (2, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'ADVICE', '경험적는란2 bbb', '안녕하세요2', 1, 1,
        'hello2', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0),
       (3, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'SYMPATHY', '경험적는란3 ccc', '안녕하세요3', 1, 1,
        'hello3', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0),
       (4, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'FACT', '경험적는란4 ddd', '안녕하세요4', 1, 1,
        'hello4', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0),
       (5, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'ADVICE', '경험적는란5 eee', '안녕하세요5', 1, 1,
        'hello5', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0),
       (6, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'ADVICE', '경험적는란6 fff', '안녕하세요6', 1, 1,
        'hello6', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0),
       (7, '2024-01-20 15:23:39', 1, '2024-01-20 15:40:14', NULL, NULL, NULL, 'ADVICE', '경험적는란7 ggg', '안녕하세요7', 1, 1,
        'hello7', 'EVALUATION_COMPLETE', 0, NULL, 0, '2024-01-20 15:40:14', 0);
-- counselor 7개 설정
UPDATE customer
SET counselor_id = 1
WHERE customer_id = 1;
UPDATE customer
SET counselor_id = 2
WHERE customer_id = 2;
UPDATE customer
SET counselor_id = 3
WHERE customer_id = 3;
UPDATE customer
SET counselor_id = 4
WHERE customer_id = 4;
UPDATE customer
SET counselor_id = 5
WHERE customer_id = 5;
UPDATE customer
SET counselor_id = 6
WHERE customer_id = 6;
UPDATE customer
SET counselor_id = 7
WHERE customer_id = 7;
-- -- customerId
INSERT INTO categories (counselor_id, consult_categories)
VALUES (1, 'DATING'),
       (1, 'ETC'),
       (2, 'BREAKUP'),
       (2, 'FEMALE_PSYCHOLOGY'),
       (3, 'MALE_PSYCHOLOGY'),
       (3, 'BEGINNING'),
       (3, 'ONE_SIDED'),
       (4, 'BOREDOM'),
       (5, 'ETC'),
       (6, 'DATING'),
       (7, 'BREAKUP');

INSERT INTO costs (counselor_id, consult_type, cost)
VALUES (1, 1, 6000),
       (1, 0, 5000),
       (2, 1, 7000),
       (2, 0, 8000),
       (3, 1, 9000),
       (3, 0, 10000),
       (4, 1, 11000),
       (4, 0, 12000),
       (5, 1, 13000),
       (5, 0, 14000),
       (6, 1, 15000),
       (6, 0, 16000),
       (7, 1, 17000),
       (7, 0, 18000);

-- INSERT INTO times (counselor_id, day, times)
-- VALUES (1, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531367E323178'),
--        (1, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531357E323078'),
--        (1, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000531387E323174000531367E313778');
--        (2, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000532367E323278'),
--        (2, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000532357E323178'),
--        (2, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000532387E323274000532367E313778'),
--        (3, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531367E323178'),
--        (3, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531357E323078'),
--        (3, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000531387E323174000531367E313778'),
--        (4, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531367E323178'),
--        (4, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531357E323078'),
--        (4, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000531387E323174000531367E313778'),
--        (5, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531367E323178'),
--        (5, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531357E323078'),
--        (5, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000531387E323174000531367E313778'),
--        (6, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531367E323178'),
--        (6, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531357E323078'),
--        (6, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000531387E323174000531367E313778'),
--        (7, 2,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531367E323178'),
--        (7, 1,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000177040000000174000531357E323078'),
--        (7, 0,
--         '0xACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A6578700000000277040000000274000531387E323174000531367E313778');
