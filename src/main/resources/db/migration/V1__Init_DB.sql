create table hibernate_sequence(next_val bigint);
insert into hibernate_sequence values (1);

create table lot
(
    id           bigint not null,
    description  varchar(2048) not null,
    end_time     DATETIME,
    filename     varchar(255),
    final_rate   double precision,
    initial_rate double precision not null,
    name         varchar(255) not null,
    start_time   DATETIME not null,
    status       varchar(255),
    time_step    double precision,
    user_id      bigint,
    primary key (id)
);

create table pricing
(
    id      bigint not null,
    bet     double precision not null,
    date    DATETIME not null,
    lot_id  bigint not null,
    user_id bigint not null,
    primary key (id)
);

create table user
(
    id              bigint not null,
    activation_code varchar(255),
    active          bit,
    balance         double precision,
    email           varchar(255) not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)
);

create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
);

alter table lot
    add constraint lot_user_fk
        foreign key (user_id) references user (id);

alter table pricing
    add constraint pricing_lot_fk
        foreign key (lot_id) references lot (id);

alter table pricing
    add constraint pricing_user_fk
        foreign key (user_id) references user (id);

alter table user_role
    add constraint user_role_user_fk
        foreign key (user_id) references user (id);

insert into user (id, username, password, active, email, balance)
values (1, 'admin', '123', true, 'auctionambey@gmail.com', 0.0);

insert into user_role(user_id, roles)
values(1, 'USER'), (1, 'ADMIN');