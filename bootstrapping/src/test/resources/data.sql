INSERT INTO test.users(id, first_name, last_name, balance, email, email_verified, enabled, password)
    VALUE (
    1,
    "Cristian",
    "Herrera",
    99.15,
    "cristianmherrera21@gmail.com",
    true,
    true,
    "{bcrypt}$2a$10$J3qMm9RkVc9l2hUUyWcS..9G.fB6mEhhUcqF0N0y6QLCAsCaBh23y" -- 12345678
    );