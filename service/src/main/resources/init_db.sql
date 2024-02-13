CREATE TABLE media_item
(
    id           BIGSERIAL PRIMARY KEY,
    created_at   TIMESTAMP(6) NOT NULL,
    type         VARCHAR(20) CHECK (type IN ('AVATAR', 'CAR_IMAGE', 'CAR_VIDEO')) NOT NULL,
    uploader_id  BIGINT NOT NULL,
    link         VARCHAR(255) NOT NULL,
    mime_type    VARCHAR(255) NOT NULL,
    preview_link VARCHAR(255)
);

CREATE TABLE users
(
    id                   BIGSERIAL PRIMARY KEY,
    birth_date           DATE,
    gender               VARCHAR(20) CHECK (gender IN ('MALE', 'FEMALE')),
    status               VARCHAR(20) CHECK (status IN ('ACTIVE', 'INACTIVE')),
    avatar_media_item_id BIGINT,
    created_at           TIMESTAMP(6),
    deleted_at           TIMESTAMP(6),
    firstname            VARCHAR(255),
    lastname             VARCHAR(255),
    password             VARCHAR(255),
    role                 VARCHAR(20) CHECK (role IN ('SUPER_ADMIN', 'ADMIN', 'OWNER', 'CLIENT')),
    username             VARCHAR(255) UNIQUE
);

CREATE TABLE personal_info
(
    user_id                       BIGINT PRIMARY KEY,
    driver_license_date_of_birth  DATE,
    driver_license_date_of_expire DATE,
    driver_license_date_of_issue  DATE,
    driver_license_code           VARCHAR(255),
    driver_license_issued_by      VARCHAR(255),
    driver_license_name           VARCHAR(255),
    driver_license_place_of_birth VARCHAR(255),
    driver_license_residence      VARCHAR(255),
    driver_license_surname        VARCHAR(255),
    driver_license_categories     SMALLINT ARRAY
);

CREATE TABLE car
(
    id           BIGSERIAL PRIMARY KEY,
    horsepower   INTEGER   NOT NULL,
    is_active    BOOLEAN   NOT NULL,
    price        FLOAT(53) NOT NULL,
    year         INTEGER   NOT NULL,
    created_at   TIMESTAMP(6),
    owner_id     BIGINT,
    manufacturer VARCHAR(255),
    model        VARCHAR(255),
    type         VARCHAR(20) CHECK (type IN ('SEDAN', 'CROSSOVER', 'HATCHBACK', 'PICKUP', 'SPORT_CAR'))
);

CREATE TABLE car_to_media_item
(
    car_id        BIGINT NOT NULL,
    media_item_id BIGINT NOT NULL,
    PRIMARY KEY (car_id, media_item_id)
);

CREATE TABLE feedback
(
    id         BIGSERIAL PRIMARY KEY,
    rating     INTEGER NOT NULL,
    created_at TIMESTAMP(6),
    request_id BIGINT,
    text       VARCHAR(255)
);

CREATE TABLE request
(
    id         BIGSERIAL PRIMARY KEY,
    car_id     INTEGER NOT NULL,
    client_id  INTEGER NOT NULL,
    created_at TIMESTAMP(6),
    date_time_from  TIMESTAMP(6),
    date_time_to    TIMESTAMP(6),
    comment    VARCHAR(255),
    status     VARCHAR(20) CHECK (status IN ('OPEN', 'PROCESSING', 'CLOSED', 'REJECTED'))
);
