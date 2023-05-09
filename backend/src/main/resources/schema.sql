DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Bewertung;

create table Users
(
    pk_id             INTEGER primary key auto_increment,
    username          varchar(50),
    password          varchar(50),
    email             varchar(50),
    email_is_verified boolean,
    profile_picture   varchar(5000000)
);

create table Bewertung
(
    pk_id      INTEGER primary key auto_increment,
    design     INTEGER,
    navigation INTEGER,
    puzzle     INTEGER,
    sammelbuch INTEGER
);

create table Sammelbucheintrag
(
    pk_id      INTEGER primary key auto_increment,
    timestamp  varchar(100),
    location   varchar(200),
    text       varchar(5000),
    bild       varchar(5000000),
    fk_user_id INTEGER,
    FOREIGN KEY (fk_user_id) REFERENCES Users (pk_id)
);

create table DefaultData
(
    pk_id           INTEGER primary key auto_increment,
    profile_picture varchar(5000000)
);

create table Spot
(
    pk_id        INTEGER primary key auto_increment,
    name         varchar(100),
    beschreibung varchar(1000),
    longitude    float,
    latitude     float
);

create table hat_besucht
(
    fk_pk_user INTEGER,
    fk_pk_spot INTEGER,
    FOREIGN KEY (fk_pk_user) REFERENCES Users (pk_id),
    FOREIGN KEY (fk_pk_spot) REFERENCES Spot (pk_id)
);