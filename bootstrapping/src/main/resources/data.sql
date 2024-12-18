-- es accecible desde cualquier schema en este caso prod & test ( y no tenemos qe especificar como: public.unaccent(..))
CREATE EXTENSION IF NOT EXISTS unaccent SCHEMA public;


-- testing funcional

DELETE
FROM users
WHERE email IN ('existente@gmail.com', 'deshabilitado@gmail.com', 'email-no-verificado@gmail.com');

INSERT INTO users(id, first_name, last_name, balance, email, email_verified, enabled, password)
VALUES
-- Email existente para login
(nextval('users_id_seq'),
 'Existente',
 'Test',
 0.00,
 'existente@gmail.com',
 true,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y' -- 12345678
),
-- deshabilitado
(nextval('users_id_seq'),
 'Deshabilitado',
 'Test',
 0.00,
 'deshabilitado@gmail.com',
 true,
 false,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y' -- 12345678
),
-- email-no-verificado
(nextval('users_id_seq'),
 'NoVerificado',
 'Test',
 0.00,
 'email-no-verificado@gmail.com',
 false,
 true,
 '{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y' -- 12345678
);