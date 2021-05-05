CREATE TABLE subscription
(
    id            BIGINT NOT NULL,
    lot_id        BIGINT NOT NULL,
    subscriber_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE subscription
    ADD CONSTRAINT subscription_lot_fk
        FOREIGN KEY (lot_id) REFERENCES lot (id);

ALTER TABLE subscription
    ADD CONSTRAINT subscription_subscriber_fk
        FOREIGN KEY (subscriber_id) REFERENCES user (id);