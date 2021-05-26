
CREATE TABLE hibernate_sequence
(
    next_val BIGINT
);

INSERT INTO hibernate_sequence (next_val)
VALUES (1);

CREATE TABLE lot
(
    id          BIGINT        NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(2048),
    start_time  DATETIME      NOT NULL,
    end_time    DATETIME,
    time_step   INT,
    initial_bet BIGINT        NOT NULL,
    final_bet   BIGINT,
    filename    VARCHAR(255),
    status      VARCHAR(255),
    user_id     BIGINT,
    PRIMARY KEY (id)
);

create table pricing
(
    id      BIGINT   NOT NULL,
    bet     BIGINT   NOT NULL,
    date    DATETIME NOT NULL,
    lot_id  BIGINT   NOT NULL,
    user_id BIGINT   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user
(
    id              BIGINT       NOT NULL,
    activation_code VARCHAR(255),
    active          BIT,
    balance         BIGINT,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    username        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id bigint NOT NULL,
    roles   VARCHAR(255)
);

ALTER TABLE lot
    ADD CONSTRAINT lot_user_fk
        FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE pricing
    ADD CONSTRAINT pricing_lot_fk
        FOREIGN KEY (lot_id) REFERENCES lot (id);

ALTER TABLE pricing
    ADD CONSTRAINT pricing_user_fk
        FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_user_fk
        FOREIGN KEY (user_id) REFERENCES user (id);

INSERT INTO user (id, activation_code, active, balance, email, password, username)
VALUES (0, null, true, 0, 'auctionambey@gmail.com', '$2y$08$Gu0OlNkXr8L5cjcvVBAl2uUgmqnbl.we3bdtvctdBrFLcn1ik3Sru',
        'admin');

INSERT INTO user_role (user_id, roles)
VALUES (0, 'USER'),
       (0, 'ADMIN');
