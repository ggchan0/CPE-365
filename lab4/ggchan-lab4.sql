-- Garrett Chan, CPE 365 Lab 4

--1a
ALTER TABLE list ADD gpa DECIMAL(3, 2);

--1b
DELETE FROM list WHERE grade = 5 or grade = 6;

--1c
INSERT INTO teachers(last, first, class) VALUES ("AP-MAWR", "GAWAIN", 120);

--1d
UPDATE list SET class = 120 WHERE (first = "JEFFRY" AND last = "FLACHS") OR (first = "TAWANNA" and last = "HUANG") or (first = "EMILE" and last = "GRUNIN");

--1e
UPDATE list SET gpa = 3.25 WHERE grade = 6;

--1f
UPDATE list SET gpa = 2.9 WHERE grade = 5 AND class = 109;

--1g
UPDATE list SET gpa = 3.5 WHERE grade = 5 AND class = 120;

--1f
UPDATE list SET gpa = gpa + 0.3 WHERE first = "AL" AND last = "GERSTEIN";

--2a
DELETE from carsData WHERE NOT (((yr BETWEEN 1979 AND 1980) AND mpg >= 20) OR (mpg >= 26 AND horsepower > 110) OR (cylinders < 8 AND accelerate < 10));

ALTER TABLE carsData DROP edispl;
ALTER TABLE carsData DROP horsepower;
ALTER TABLE carsData DROP weight;

--2c
DELETE from carsData WHERE cylinders <= 5;
