DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Bewertung;

create table Users (
                      pk_id INTEGER primary key auto_increment,
                      username varchar(50),
                      password varchar(50),
                      email varchar(50),
                      email_is_verified boolean
);

create table Bewertung (
                           pk_id INTEGER primary key auto_increment,
                           design INTEGER,
                           navigation INTEGER,
                           puzzle INTEGER,
                           sammelbuch INTEGER
);