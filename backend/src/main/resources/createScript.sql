create table User (
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

insert into User (username, password, email, email_is_verified) values ('Daniel Pillwein', 'gutesPasswort', 'dani@gmail.com', true);
insert into User (username, password, email, email_is_verified) values ('Lukas Schodl', 'besseresPasswort', 'luki@gmail.com', true);
insert into User (username, password, email, email_is_verified) values ('Markus Wizany', 'schlechtesPasswort', 'markus@gmail.com', false);
