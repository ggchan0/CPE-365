/* Garrett Chan */
CREATE TABLE list(
   last VARCHAR(50) NOT NULL,
   first VARCHAR(50) NOT NULL,
   grade INTEGER NOT NULL,
   class INTEGER NOT NULL REFERENCES teachers(class)
);

CREATE TABLE teachers(
   last VARCHAR(50) NOT NULL,
   first VARCHAR(50) NOT NULL,
   class INTEGER NOT NULL
);