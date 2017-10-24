-- ggchan
CREATE TABLE campuses (
   id INTEGER NOT NULL,
   campus VARCHAR(100) NOT NULL,
   loc VARCHAR(100) NOT NULL,
   county VARCHAR(100) NOT NULL,
   year INTEGER NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE fees (
   id INTEGER NOT NULL REFERENCES campuses(id),
   year INTEGER NOT NULL,
   cost INTEGER NOT NULL
);

CREATE TABLE degrees (
   year INTEGER NOT NULL,
   id INTEGER NOT NULL REFERENCES campuses(id),
   degs INTEGER NOT NULL
);

CREATE TABLE disciplines(
   id INTEGER NOT NULL,
   name VARCHAR(100) NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE disciplineEnrollments (
   id INTEGER NOT NULL REFERENCES campuses(id),
   discipline INTEGER NOT NULL REFERENCES disciplines(id),
   year INTEGER NOT NULL,
   undergrad INTEGER NOT NULL,
   grad INTEGER NOT NULL
);

CREATE TABLE enrollments (
   id INTEGER REFERENCES campuses(id),
   year INTEGER NOT NULL,
   total INTEGER NOT NULL,
   fulltimes INTEGER NOT NULL
);

CREATE TABLE faculty (
   id INTEGER NOT NULL REFERENCES campuses(id),
   year INTEGER NOT NULL,
   num FLOAT NOT NULL
);