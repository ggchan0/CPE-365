-- ggchans
CREATE TABLE reservations(
   code INTEGER NOT NULL,
   room CHAR(3) NOT NULL REFERENCES rooms(room),
   checkin VARCHAR(10) NOT NULL,
   checkout VARCHAR(10) NOT NULL,
   rate FLOAT NOT NULL,
   first VARCHAR(45) NOT NULL,
   last VARCHAR(45) NOT NULL,
   adults INTEGER NOT NULL,
   kids INTEGER NOT NULL,
   PRIMARY KEY(code),
   UNIQUE(code)
);

CREATE TABLE rooms(
   room CHAR(3) NOT NULL,
   name VARCHAR(100) NOT NULL,
   beds INTEGER NOT NULL,
   bedType VARCHAR(50) NOT NULL,
   occupancy INTEGER NOT NULL,
   basePrice INTEGER NOT NULL,
   decor VARCHAR(100) NOT NULL,
   PRIMARY KEY(room),
   UNIQUE(room)
);
