-- ggchan
CREATE TABLE albums(
   id INTEGER NOT NULL,
   title VARCHAR(100) NOT NULL,
   year INTEGER NOT NULL,
   label VARCHAR(100) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE band(
   id INTEGER NOT NULL,
   first VARCHAR(50) NOT NULL,
   last VARCHAR(50) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE instruments(
   songid INTEGER NOT NULL REFERENCES songs(id),
   memberid INTEGER NOT NULL REFERENCES members(id),
   instrument VARCHAR(50) NOT NULL
);

CREATE TABLE performances(
   songid INTEGER NOT NULL REFERENCES songs(id),
   memberid INTEGER NOT NULL REFERENCES members(id),
   position VARCHAR(50) NOT NULL
);

CREATE TABLE songs(
   id INTEGER NOT NULL,
   title VARCHAR(100) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE tracklists(
   id INTEGER NOT NULL,
   position VARCHAR(50) NOT NULL,
   songid INTEGER NOT NULL REFERENCES songs(id),
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE vocals(
   songid INTEGER NOT NULL REFERENCES songs(id),
   memberid INTEGER NOT NULL REFERENCES members(id),
   type VARCHAR(100) NOT NULL
);
