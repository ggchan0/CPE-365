-- ggchan
CREATE TABLE customers (
   id INTEGER NOT NULL,
   first VARCHAR(50) NOT NULL,
   last VARCHAR(15) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE goods (
   id VARCHAR(50) NOT NULL,
   flavor VARCHAR(100) NOT NULL,
   food VARCHAR(100) NOT NULL,
   price FLOAT NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id)
);

CREATE TABLE items (
   num INTEGER NOT NULL REFERENCES receipts(num),
   ordinal INTEGER NOT NULL,
   good VARCHAR(50) REFERENCES goods(id),
   UNIQUE(num, ordinal)
);

CREATE TABLE receipts (
   num INTEGER NOT NULL,
   day DATE NOT NULL,
   customer INTEGER NOT NULL REFERENCES customers(id),
   PRIMARY KEY(num),
   UNIQUE(num)
);