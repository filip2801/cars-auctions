create sequence auction_bid_seq;
create sequence auction_seq;
create sequence auction_subscription_seq;
create sequence car_seq;
create sequence inspection_appointment_seq;
create sequence notification_seq;
create sequence users_seq;

create table auction
(
    anchor_bid             integer      not null,
    highest_bid            integer,
    car_id                 bigint       not null,
    expected_end_time      timestamp(6) not null,
    id                     bigint       not null primary key,
    leading_bidder_id      bigint,
    start_time             timestamp(6) not null,
    version                bigint       not null,
    customer_email_address varchar(255),
    status                 varchar(255) not null
);


create table auction_bid
(
    id         bigint       not null primary key,
    bid_value  integer      not null,
    auction_id bigint       not null,
    dealer_id  bigint       not null,
    time       timestamp(6) not null,
    status     varchar(255) not null
);

create table auction_subscription
(
    id          bigint not null primary key,
    car_make_id bigint not null,
    dealer_id   bigint  not null
);

create table car
(
    id                     bigint       not null primary key,
    manufacturing_year     integer      not null,
    registration_year      integer      not null,
    make_id                bigint       not null,
    model_id               bigint       not null,
    variant_id             bigint       not null,
    winner_id              bigint,
    customer_email_address varchar(255) not null,
    status                 varchar(255) not null
);

create table inspection_appointment
(
    car_id      bigint       not null,
    id          bigint       not null primary key,
    location_id bigint       not null,
    time        timestamp(6) not null,
    status      varchar(255) not null
);

create table notification
(
    id         bigint       not null primary key,
    auction_id bigint       not null,

    user_id    bigint       not null,
    type       varchar(255) not null
);

create table users
(
    id       bigint       not null primary key,
    role     varchar(255) not null,
    password varchar(255) not null,
    username varchar(255) not null
);


