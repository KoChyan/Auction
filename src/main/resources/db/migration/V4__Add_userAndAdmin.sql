INSERT INTO user (id, activation_code, active, balance, email, password, username)
VALUES (0, null, true, 0, 'defaultUser@gmail.com', '$2y$08$Gu0OlNkXr8L5cjcvVBAl2uUgmqnbl.we3bdtvctdBrFLcn1ik3Sru',
        'user');

INSERT INTO user_role (user_id, roles)
VALUES (0, 'USER');

INSERT INTO user (id, activation_code, active, balance, email, password, username)
    VALUES (0, null, true, 0, 'defaultAdmin@gmail.com', '$2y$08$Gu0OlNkXr8L5cjcvVBAl2uUgmqnbl.we3bdtvctdBrFLcn1ik3Sru',
        'admin');

INSERT INTO user_role (user_id, roles)
VALUES (0, 'ADMIN');
