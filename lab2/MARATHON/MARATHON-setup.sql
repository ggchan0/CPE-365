CREATE TABLE marathon(
   place INTEGER NOT NULL,
   total TIME NOT NULL,
   pace TIME NOT NULL,
   groupPlace INTEGER NOT NULL,
   grp VARCHAR(30) NOT NULL,
   age INTEGER NOT NULL,
   sex CHAR(1) NOT NULL,
   bibNum INTEGER NOT NULL,
   first VARCHAR(50) NOT NULL,
   last VARCHAR(50) NOT NULL,
   town VARCHAR(40) NOT NULL,
   state CHAR(2) NOT NULL,
   PRIMARY KEY(place),
   UNIQUE(place)
);