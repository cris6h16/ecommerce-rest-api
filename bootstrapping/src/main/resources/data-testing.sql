-- es accecible desde cualquier schema en este caso prod & test ( y no tenemos qe especificar como: public.unaccent(..))
CREATE EXTENSION IF NOT EXISTS unaccent SCHEMA public;



-- TODO: usar para test environment un usuario que tenga solo los permisos para el schema que corresponde a test. para evitar por error borrar la base de datos
-- testing funcional
TRUNCATE TABLE cart_items CASCADE;
TRUNCATE TABLE carts CASCADE;
TRUNCATE TABLE products CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE email_verification CASCADE;
TRUNCATE TABLE product_images CASCADE;
TRUNCATE TABLE categories CASCADE;

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
        4,
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
        0.6);
