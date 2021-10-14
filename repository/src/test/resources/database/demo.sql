INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name1', 'description1', 15.5, 25, '2021-10-03 22:12:00', '2021-10-03 22:12:00');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name2', 'description2', 33.9, 66, '2021-10-03 22:12:00', '2021-10-03 22:12:00');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name3', 'description3', 98.5, 100, '2021-10-03 22:12:00', '2021-10-03 22:12:00');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('name4', 'description4', 10.5, 15, '2021-10-03 22:12:00', '2021-10-03 22:12:00');

INSERT INTO tag (name)
VALUES ('tag1');

INSERT INTO tag (name)
VALUES ('tag2');

INSERT INTO tag (name)
VALUES ('tag3');

INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id)
VALUES (1, 1);