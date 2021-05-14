CREATE TABLE subscription
(
    lot_id        BIGINT NOT NULL REFERENCES lot(id),
    subscriber_id BIGINT NOT NULL REFERENCES user(id),
    PRIMARY KEY (lot_id, subscriber_id)
);
