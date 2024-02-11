create table media_items
(
    id           bigserial not null,
    created_at   date,
    type         varchar(20) check (type in ('AVATAR', 'CAR_IMAGE', 'CAR_VIDEO')),
    uploader_id  bigint,
    link         varchar(255),
    mime_type    varchar(255),
    preview_link varchar(255),
    primary key (id)
);

create table users
(
    id                   bigserial not null,
    birth_date           date,
    gender               varchar(20) check (gender in ('MALE', 'FEMALE')),
    status               varchar(20) check (status in ('ACTIVE', 'INACTIVE')),
    avatar_media_item_id bigint references media_items (id),
    created_at           timestamp(6),
    deleted_at           timestamp(6),
    firstname            varchar(255),
    lastname             varchar(255),
    password             varchar(255),
    role                 varchar(20) check (role in ('SUPER_ADMIN', 'ADMIN', 'OWNER', 'CLIENT')),
    username             varchar(255) unique,
    primary key (id)
);

create table personal_info
(
    user_id                       bigint not null,
    driver_license_date_of_birth  date,
    driver_license_date_of_expire date,
    driver_license_date_of_issue  date,
    driver_license_code           varchar(255),
    driver_license_issued_by      varchar(255),
    driver_license_name           varchar(255),
    driver_license_place_of_birth varchar(255),
    driver_license_residence      varchar(255),
    driver_license_surname        varchar(255),
    driver_license_categories     smallint array,
    primary key (user_id)
);

create table cars
(
    id           bigserial not null,
    horsepower   integer   not null,
    is_active    boolean   not null,
    price        float(53) not null,
    year         integer   not null,
    created_at   timestamp(6),
    user_id      bigint,
    manufacturer varchar(255),
    model        varchar(255),
    type         varchar(20) check (type in ('SEDAN', 'CROSSOVER', 'HATCHBACK', 'PICKUP', 'SPORT_CAR')),
    primary key (id)
);

create table car_to_media_item
(
    car_id        bigint not null,
    media_item_id bigint not null,
    primary key (car_id, media_item_id)
);

create table feedbacks
(
    id         bigserial not null,
    rating     integer   not null,
    created_at timestamp(6),
    request_id bigint,
    user_id    bigint,
    text       varchar(255),
    primary key (id)
);

create table requests
(
    id         bigserial not null,
    car_id     integer   not null,
    client_id  integer   not null,
    created_at timestamp(6),
    date_from  timestamp(6),
    date_to    timestamp(6),
    comment    varchar(255),
    status     varchar(20) check (status in ('OPEN', 'PROCESSING', 'CLOSED', 'REJECTED')),
    primary key (id)
);
