/* Garrett Chan */
CREATE TABLE grapes(
   id INTEGER NOT NULL,
   variety VARCHAR(50) NOT NULL,
   color VARCHAR(50) NOT NULL,
   PRIMARY KEY(id) 
);

CREATE TABLE appelations(
   id INTEGER NOT NULL,
   appelation VARCHAR(100) NOT NULL,
   county VARCHAR(100) NOT NULL,
   state VARCHAR(100) NOT NULL,
   area VARCHAR(100) NOT NULL,
   isAva VARCHAR(10) NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE wine(
   id INTEGER NOT NULL,
   grape VARCHAR(50) NOT NULL REFERENCES grape(variety),
   winery VARCHAR(100) NOT NULL,
   appelation VARCHAR(100) NOT NULL REFERENCES appelations(appelation),
   name VARCHAR(100),
   year INTEGER NOT NULL,
   price INTEGER NOT NULL,
   cases INTEGER,
   PRIMARY KEY(id)
);
