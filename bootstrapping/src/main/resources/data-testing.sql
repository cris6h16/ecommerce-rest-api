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
VALUES

-- Email existente para login
(1,
 'Existente',
 'Test',
 0.00,
 'existente@gmail.com',
 true,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_USER'),

-- deshabilitado
(2,
 'Deshabilitado',
 'Test',
 0.00,
 'deshabilitado@gmail.com',
 true,
 false,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_USER'),

-- email-no-verificado
(3,
 'NoVerificado',
 'Test',
 0.00,
 'email-no-verificado@gmail.com',
 false,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_USER'),

-- No habilitado (VENDEDOR)
(4,
 'NoHabilitado',
 'Test',
 0.00,
 'vededor_deshabilitado@gmail.com',
 true,
 false,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_SELLER'),

-- No verificado (VENDEDOR)
(5,
 'NoVerificado',
 'Test',
 0.00,
 'vededor_email_no_verificado@gmail.com',
 false,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_SELLER'),

-- Vendedor (habilitado y verificado)
(6,
 'Super',
 'Tienda',
 0.00,
 'super@tienda.com',
 true,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_SELLER');


-- testing funcional
INSERT INTO email_verification(id, email, code, action_type, created_at, expires_at, used)
VALUES (1,
        'email-no-verificado@gmail.com',
        'ZJM307',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (2,
        'existente@gmail.com',
        'AXY184',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours',
        false),
       (3,
        'existente@gmail.com',
        'FLAU5C',
        'VERIFY_EMAIL',
        now(),
        now(),
        false),
       (4,
        'existente@gmail.com',
        '04S8R5',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (5,
        'deshabilitado@gmail.com',
        '4A5C1V',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours',
        false),
       (6,
        'existente@gmail.com',
        '4J5G44',
        'RESET_PASSWORD',
        now(),
        now(),
        false);


-- - --- -- -  - -- - - - - - -- -- - -- - - - --- -- - -


-- Insertar categorías
INSERT INTO categories(id, name)
VALUES (1, 'Accesorios'),
       (2, 'Ropa'),
       (3, 'Zapatos'),
       (4, 'Celulares');

-- Insertar productos
INSERT INTO products(length_cm,
                     height_cm,
                     weight_pounds,
                     width_cm,
                     price,
                     stock,
                     category_id,
                     id,
                     user_id,
                     description,
                     name)
VALUES (23,
        1,
        2,
        3,
        9.25,
        99,
        1,
        1,
        6,
        '1 metro de largo, 480 Mbps de velocidad de transferencia de datos, compatible con USB 2.0 y 1.1',
        'Cable USB 3.0 marca Samsung'),

       (23,
        6,
        5,
        0,
        1210,
        4,
        4,
        2,
        6,
        'Pantalla de 6.8 pulgadas, 12 GB de RAM, 128 GB de almacenamiento, cámara de 108 MP',
        'Celular Samsung Galaxy S21 Ultra 5G'),

       (23,
        0,
        1,
        0,
        17.99,
        71,
        2,
        3,
        6,
        'Camisa de vestir minimalista para hombre, sámsúng, talla M, color azul',
        'Camisa de vestir'),

       (23,
        9,
        10,
        11,
        35.99,
        12,
        3,
        4,
        6,
        'Zapatos de vestir para hombre de cuero, talla 42, color negro',
        'Zapatos de vestir');
