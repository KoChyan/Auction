
CREATE TABLE comment
(
    id        BIGINT NOT NULL,
    text      VARCHAR(1024),
    date      DATETIME,
    author_id BIGINT NOT NULL ,
    lot_id    BIGINT NOT NULL ,
    PRIMARY KEY (id)
);

ALTER TABLE comment
    ADD CONSTRAINT comment_user_fk
        FOREIGN KEY (author_id) REFERENCES user (id);

ALTER TABLE comment
    ADD CONSTRAINT comment_lot_fk
        FOREIGN KEY (lot_id) REFERENCES lot (id);

