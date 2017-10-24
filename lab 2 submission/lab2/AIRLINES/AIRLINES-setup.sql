-- ggchan
CREATE TABLE airlines (
   id INTEGER NOT NULL,
   airline VARCHAR(50) NOT NULL,
   abbreviation VARCHAR(15),
   country VARCHAR(15) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE airports (
   city VARCHAR(100) NOT NULL,
   airportCode CHAR(3) NOT NULL,
   airportName VARCHAR(100) NOT NULL,
   country VARCHAR(100) NOT NULL,
   countryAbbrev VARCHAR(20),
   PRIMARY KEY(airportCode),
   UNIQUE(airportCode)
);

CREATE TABLE flights (
   airline INTEGER NOT NULL REFERENCES airlines(id),
   flightNo INTEGER NOT NULL,
   source CHAR(3) REFERENCES airports100(airportCode),
   dest CHAR(3) REFERENCES airports100(airportCode),
   UNIQUE(airline, flightNo)
);