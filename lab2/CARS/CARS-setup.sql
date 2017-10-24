/* Garrett Chan */
CREATE TABLE continents (
   id INTEGER NOT NULL,
   continent VARCHAR(50) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE countries (
   id INTEGER NOT NULL,
   country VARCHAR(100) NOT NULL,
   continent INTEGER NOT NULL REFERENCES continents(id),
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE carsData (
   id INTEGER NOT NULL,
   mpg INTEGER,
   cylinders INTEGER,
   edispl INTEGER,
   horsepower INTEGER,
   weight INTEGER,
   accelerate FLOAT,
   year INTEGER,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE carMakers (
   id INTEGER NOT NULL,
   maker VARCHAR(100) NULL,
   fullname VARCHAR(100) NOT NULL,
   country INTEGER NOT NULL REFERENCES countries(id)
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE modelList (
   id INTEGER NOT NULL,
   maker INTEGER NOT NULL REFERENCES carMakers(id),
   model VARCHAR(150) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(model)
);

CREATE TABLE carNames (
   id INTEGER NOT NULL REFERENCES carsData(id),
   model VARCHAR(100) NOT NULL REFERENCES modelList(model),
   make VARCHAR(120) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);