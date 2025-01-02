-- es accecible desde cualquier schema en este caso prod & test ( y no tenemos qe especificar como: public.unaccent(..))
CREATE EXTENSION IF NOT EXISTS unaccent SCHEMA public;


-- testing funcional

DELETE
FROM users
WHERE email IN ('existente@gmail.com', 'deshabilitado@gmail.com', 'email-no-verificado@gmail.com');

INSERT INTO users(id, first_name, last_name, balance, email, email_verified, enabled, password, authority)
VALUES
-- Email existente para login
(nextval('users_id_seq'),
 'Existente',
 'Test',
 0.00,
 'existente@gmail.com',
 true,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_USER'),
-- deshabilitado
(nextval('users_id_seq'),
 'Deshabilitado',
 'Test',
 0.00,
 'deshabilitado@gmail.com',
 true,
 false,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_USER'),
-- email-no-verificado
(nextval('users_id_seq'),
 'NoVerificado',
 'Test',
 0.00,
 'email-no-verificado@gmail.com',
 false,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
 'ROLE_USER');

-- testing funcional
INSERT INTO email_verification(id, email, code, action_type, created_at, expires_at)
VALUES (nextval('email_verification_id_seq'),
        'email-no-verificado@gmail.com',
        'ZJM3076MFJ',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours'),
       (nextval('email_verification_id_seq'),
        'existente@gmail.com',
        'AXY1840Y10',
        'VERIFY_EMAIL',
        now(),
        now() + interval '15 hours'),
       (nextval('email_verification_id_seq'),
        'existente@gmail.com',
        'FLAU5C4A99',
        'VERIFY_EMAIL',
        now(),
        now()),
       (nextval('email_verification_id_seq'),
        'existente@gmail.com',
        '04S8R5V340',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours'),
       (nextval('email_verification_id_seq'),
        'deshabilitado@gmail.com',
        '4A5C1V2A66',
        'RESET_PASSWORD',
        now(),
        now() + interval '15 hours'),
       (nextval('email_verification_id_seq'),
        'existente@gmail.com',
        '4J5G44H8SW',
        'RESET_PASSWORD',
        now(),
        now());


-- - --- -- -  - -- - - - - - -- -- - -- - - - --- -- - -

DELETE
FROM products
WHERE name IN ('Cable USB 3.0 marca Samsung', 'Camisa de vestir', 'Zapatos de vestir', 'Celular Samsung Galaxy S21 Ultra 5G');
DELETE
FROM categories
WHERE name IN ('Accesorios', 'Ropa', 'Zapatos', 'Celulares');
DELETE
FROM users
WHERE email IN ('super@tienda.com');

-- Insertar un usuario con id generado automáticamente
INSERT INTO users(first_name, last_name, balance, email, email_verified, enabled, password, authority)
VALUES ('Super',
        'Tienda',
        0.00,
        'super@tienda.com',
        true,
        true,
        '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y', -- 12345678
        'ROLE_SELLER');

-- Insertar categorías
INSERT INTO categories(id, name)
VALUES (1, 'Accesorios'),
       (2, 'Ropa'),
       (3, 'Zapatos'),
       (4, 'Celulares');

-- Insertar productos
INSERT INTO products(approx_height_cm,
                     approx_weight_lb,
                     approx_width_cm,
                     price,
                     stock,
                     category_id,
                     id,
                     user_id,
                     description,
                     name)
VALUES (1,
        2,
        3,
        9.25,
        99,
        1,
        nextval('products_id_seq'),
        currval('users_id_seq'),
        '1 metro de largo, 480 Mbps de velocidad de transferencia de datos, compatible con USB 2.0 y 1.1',
        'Cable USB 3.0 marca Samsung'),
       (6,
        5,
        0,
        1210,
        4,
        4,
        nextval('products_id_seq'),
        currval('users_id_seq'),
        'Pantalla de 6.8 pulgadas, 12 GB de RAM, 128 GB de almacenamiento, cámara de 108 MP',
        'Celular Samsung Galaxy S21 Ultra 5G'),
       (5,
        6,
        7, 17.99,
        71,
        2,
        nextval('products_id_seq'),
        currval('users_id_seq'),
        'Camisa de vestir minimalista para hombre, sámsúng, talla M, color azul',
        'Camisa de vestir'),
       (9,
        10,
        11,
        35.99,
        12,
        3,
        nextval('products_id_seq'),
        currval('users_id_seq'),
        'Zapatos de vestir para hombre de cuero, talla 42, color negro',
        'Zapatos de vestir');
