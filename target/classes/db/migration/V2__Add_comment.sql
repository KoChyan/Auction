
CREATE TABLE comment
(
    id        BIGINT NOT NULL,
    text      VARCHAR(1024),
    date    DATETIME,
    author_id BIGINT,
    lot_id    BIGINT,
    primary key (id)
);

ALTER TABLE comment
    ADD CONSTRAINT comment_user_fk
        FOREIGN KEY (author_id) REFERENCES user (id);

ALTER TABLE comment
    ADD CONSTRAINT comment_lot_fk
        FOREIGN KEY (lot_id) REFERENCES lot (id);

