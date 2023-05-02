insert into Users (username, password, email, email_is_verified, profile_picture) values ('Daniel Pillwein', 'gutesPasswort', 'dani@gmail.com', true, 'THIS IS A GOOD PROFILE PICTURE WITH THIS DATA: jabdbubUZGFBf783JNB(/bn78B fUUB(/UUZHZb/(B(//VTCerRZHIAgszufguazhBIfhiADf');
insert into Users (username, password, email, email_is_verified) values ('Lukas Schodl', 'besseresPasswort', 'luki@gmail.com', true);
insert into Users (username, password, email, email_is_verified) values ('Markus Wizany', 'schlechtesPasswort', 'markus@gmail.com', false);

insert into Sammelbucheintrag (timestamp, location, text, bild, fk_user_id) values ('2004-06-14T23:34:30', 'Wien', 'Dies ist ein text für wien', 'Dies sind base64 daten eines bildes', 1);
insert into Sammelbucheintrag (timestamp, location, text, bild, fk_user_id) values ('2005-06-14T23:34:30', 'Wien', 'Dies ist ein text für wien', 'Dies sind base64 daten eines bildes', 1);
insert into Sammelbucheintrag (timestamp, location, text, bild, fk_user_id) values ('2006-06-14T23:34:30', 'Stephansplatz', 'Dies ist ein text für Stephansplatz', 'Dies sind base64 daten eines bildes', 1);
insert into Sammelbucheintrag (timestamp, location, text, bild, fk_user_id) values ('2007-06-14T23:34:30', 'Wien', 'Dies ist ein text für wien', 'Dies sind base64 daten eines bildes', 1);
insert into Sammelbucheintrag (timestamp, location, text, bild, fk_user_id) values ('2008-06-14T23:34:30', 'Wien', 'Dies ist ein text für wien', 'Dies sind base64 daten eines bildes', 2);
