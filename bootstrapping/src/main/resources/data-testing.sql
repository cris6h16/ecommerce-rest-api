-- es accecible desde cualquier schema en este caso prod & test ( y no tenemos qe especificar como: public.unaccent(..))
CREATE EXTENSION IF NOT EXISTS unaccent SCHEMA public;



-- TODO: usar para test environment un usuario que tenga solo los permisos para el schema que corresponde a test. para evitar por error borrar la base de datos
-- testing funcional
DELETE
FROM addresses;
DELETE
FROM cart_items;
DELETE
FROM carts;
DELETE
FROM product_images;
DELETE
FROM products;
DELETE
FROM users;
DELETE
FROM email_verification;
DELETE
FROM categories;

SELECT setval('addresses_id_seq', 100, true);
SELECT setval('users_id_seq', 100, true);
SELECT setval('products_id_seq', 100, true);
SELECT setval('email_verification_id_seq', 100, true);
SELECT setval('categories_id_seq', 100, true);
SELECT setval('carts_id_seq', 100, true);
SELECT setval('cart_items_id_seq', 100, true);



INSERT INTO users(id, first_name, last_name, balance, email, email_verified, enabled, password, authority)
VALUES (1,
        'VHabilitado',
        'Test',
        0.00,
        'vhabilitado1@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_SELLER'),
       (2,
        'UHabilitado',
        'Test',
        0.00,
        'uhabilitado@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (3,
        'VNoVerificado',
        'Test',
        0.00,
        'vhabilitado-noverificado@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_SELLER'),
       (4,
        'VDeshabilitado',
        'Test',
        0.00,
        'vdeshabilitado@gmail.com',
        true,
        false,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_SELLER');


INSERT INTO email_verification(id, email, code, action_type, created_at, expires_at, used)
VALUES (1,
        'vhabilitado1@gmail.com',
        'ZJM307',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (2,
        'vdeshabilitado@gmail.com',
        'A1T184',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (3,
        'vhabilitado1@gmail.com',
        'FLAU5C',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        true),
       (4,
        'vhabilitado1@gmail.com',
        '4A5C1V',
        'VERIFY_EMAIL',
        now(),
        now(),
        false),
       (5,
        'vhabilitado1@gmail.com',
        'APC75X',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (6,
        'vdeshabilitado@gmail.com',
        'K1X00A',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (7,
        'vhabilitado1@gmail.com',
        '0S4C86',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        true),
       (8,
        'vhabilitado1@gmail.com',
        'F7HAB0',
        'RESET_PASSWORD',
        now(),
        now(),
        false);


INSERT INTO categories(id, name)
VALUES (1, 'Accesorios'),
       (2, 'Celulares'),
       (3, 'Relojes');

INSERT INTO products(id,
                     name,
                     description,
                     price,
                     stock,
                     category_id,
                     user_id,
                     height_cm,
                     width_cm,
                     length_cm,
                     weight_pounds)
VALUES (1,
        'Cargador de batería AA',
        'Cargador compacto y eficiente para baterías AA de NiMH o NiCd. Compatible con controles remotos, cámaras, linternas y más. ',
        9.25,
        12,
        1,
        1,
        7,
        5,
        14,
        0.4),
       (2,
        'Celular Samsung Galaxy S21 Ultra 5G',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        1210,
        40,
        2,
        1,
        5,
        5,
        15,
        1),
       (3,
        'Samsung Smart Watch 5 Pro (amoled)',
        'Reloj inteligente resistente y de alto rendimiento. Monitorea tu salud, actividades y notificaciones, todo con un diseño elegante.',
        49.99,
        7,
        3,
        1,
        5,
        5,
        5,
        0.2),
       (4,
        'Micrófono Lavalier inalámbrico',
        'Micrófono compacto y discreto de marca Samsung. Ideal para grabaciones, entrevistas y presentaciones. Ofrece audio claro y está disponible en versiones inalámbrica o con cable.',
        117,
        10,
        1,
        1,
        10,
        10,
        10,
        0.6),


       (10,
        'Honor 7 Pro',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (11,
        'Redmi Note 10 Pro',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (12,
        'Honor 20 Plus',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (13,
        'Xiomi 9 lite',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (14,
        'Honor 007',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (15,
        'Honor 008',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (16,
        'Honor 009',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (17,
        'Honor 010',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (18,
        'Honor 011',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (19,
        'Honor 012',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (20,
        'Honor 013',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2),
       (21,
        'Honor 014',
        'Potente smartphone con pantalla Dynamic AMOLED de 6.8", cámaras profesionales y batería de 5000 mAh para todo el día. Compatible con carga rápida e inalámbrica.',
        170,
        90,
        2,
        1,
        10,
        5,
        20,
        0.2);


INSERT INTO carts(id, user_id)
VALUES (1, 2);

INSERT INTO cart_items(id, product_id, cart_id, quantity)
VALUES (1, 2, 1, 3),
       (2, 3, 1, 6);

INSERT INTO addresses(id, user_id, mobile_number, country, city, state, reference, street)
VALUES (10, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (11, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (12, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (13, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (14, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (15, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (16, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (17, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (18, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (19, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (20, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre'),
       (21, 2, '+593960009070', 'Ecuador', 'Tena', 'Napo', 'Frente a la iglesia', 'Av. 15 de Noviembre');

-- Pruebas de Rendimiento
INSERT INTO email_verification(id, email, code, action_type, created_at, expires_at, used)
VALUES (10,
        'pfunc1@gmail.com',
        'F5A60X',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (11,
        'pfunc2@gmail.com',
        '4GA2V6',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (12,
        'pfunc3@gmail.com',
        '7XHSJD',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (13,
        'pfunc4@gmail.com',
        'C5A7B6',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (14,
        'pfunc5@gmail.com',
        'H0XG44',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (15,
        'pfunc6@gmail.com',
        'U7C363',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (16,
        'pfunc7@gmail.com',
        'D5S8D4',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (17,
        'pfunc8@gmail.com',
        'F5S2X0',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (18,
        'pfunc9@gmail.com',
        'X4SXX5',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (19,
        'pfunc10@gmail.com',
        'A5XD4A',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (20,
        'pfunc11@gmail.com',
        '65DS4A',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (21,
        'pfunc12@gmail.com',
        '6S5X1S',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (22,
        'pfunc1@gmail.com',
        'CC4WH2',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (23,
        'pfunc2@gmail.com',
        '6FR5J4',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (24,
        'pfunc3@gmail.com',
        'B41AD6',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (25,
        'pfunc4@gmail.com',
        'SW658F',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (26,
        'pfunc5@gmail.com',
        '6153RB',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (27,
        'pfunc6@gmail.com',
        'M5HJ1U',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (28,
        'pfunc7@gmail.com',
        'B4NJU0',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (29,
        'pfunc8@gmail.com',
        '89DFGF',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (30,
        'pfunc9@gmail.com',
        'GTGW7E',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (31,
        'pfunc10@gmail.com',
        'BG489A',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (32,
        'pfunc11@gmail.com',
        '61RYJ6',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (33,
        'pfunc12@gmail.com',
        'D8HN65',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false);



INSERT INTO users(id, first_name, last_name, balance, email, email_verified, enabled, password, authority)
VALUES (10,
        'PFunc1',
        'Test',
        0.00,
        'pfunc1@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (11,
        'PFuncs2',
        'Test',
        0.00,
        'pfunc2@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (12,
        'PFunc3',
        'Test',
        0.00,
        'pfunc3@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (13,
        'PFunc4',
        'Test',
        0.00,
        'pfunc4@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (14,
        'PFunc5',
        'Test',
        0.00,
        'pfunc5@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (15,
        'PFunc6',
        'Test',
        0.00,
        'pfunc6@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (16,
        'PFunc7',
        'Test',
        0.00,
        'pfunc7@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (17,
        'PFunc8',
        'Test',
        0.00,
        'pfunc8@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (18,
        'PFunc9',
        'Test',
        0.00,
        'pfunc9@gmail.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (19,
        'PFunc10',
        'Test',
        0.00,
        'pfunc10@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (20,
        'PFunc11',
        'Test',
        0.00,
        'pfunc11@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER'),
       (21,
        'PFunc12',
        'Test',
        0.00,
        'pfunc12@gmail.com',
        false,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_USER');



