--Garrett Chan
--CPE-365 Von Dollen

--BAKERY-1

SELECT c.first,
       c.last
FROM receipts r,
     customers c,
     goods g,
     items i
WHERE r.customer = c.id
  AND i.num = r.num
  AND r.d >= '2007-10-01'
  AND r.d <= '2007-10-31'
GROUP BY c.first,
         c.last
HAVING Sum(g.price) =
  (SELECT Max(allCustomers.total)
   FROM
     (SELECT c1.first AS "First",
             c1.last AS "Last",
             Sum(g1.price) AS "total"
      FROM receipts r1,
           customers c1,
           goods g1,
           items i1
      WHERE r1.customer = c1.id
        AND i1.num = r1.num
        AND g1.id = i1.good
        AND r1.d >= '2007-10-01'
        AND r1.d <= '2007-10-31'
      GROUP BY c1.first,
               c1.last) allCustomers);

--BAKERY-2

SELECT c.first,
       c.last
FROM customers c
WHERE c.id NOT IN
    (SELECT c.id
     FROM customers c1,
          receipts r1
     WHERE c1.id = r1.customer
       AND r.d >= '2007-10-05'
       AND r.d <= '2007-10-11')
ORDER BY c.last;

--BAKERY-3

SELECT DISTINCT allCustomers.first,
                allCustomers.last
FROM
  (SELECT c.first AS FIRST,
          c.last AS LAST
   FROM receipts r,
        customers c,
        goods g,
        items i
   WHERE r.customer = c.id
     AND g.id = i.good
     AND r.d >= '2007-10-01'
     AND r.d <= '2007-10-31') allCustomers
LEFT JOIN
  (SELECT c1.first AS "First",
          c1.last AS "Last"
   FROM receipts r1,
        customers c1,
        goods g1,
        items i1
   WHERE i1.num = r1.num
     AND g1.id = i1.good
     AND g1.food = 'Twist'
     AND r1.d >= '2007-10-01'
     AND r1.d <= '2007-10-31') twistCustomers ON allCustomers.first = twistCustomers.first
AND allCustomers.last = twistCustomers.last
WHERE twistCustomers.first IS NULL
  AND twistCustomers.last IS NULL
ORDER BY allCustomers.last;

--CARS-1

SELECT cn.make,
       cd.yr
FROM carsdata cd,
     carnames cn
WHERE cd.id = cn.id
  AND mpg =
    (SELECT Max(mpg)
     FROM carsdata);

--CARS-2

SELECT cn.make,
       cd.yr
FROM carsdata cd,
     carnames cn
WHERE cd.id = m.id
  AND cd.accelerate =
    (SELECT Max(allacceleration)
     FROM
       (SELECT cd1.accelerate AS "AllAcceleration"
        FROM carsdata cd1,
             carnames cn1
        WHERE cd1.id = cn1.id
          AND cd1.mpg =
            (SELECT Max(mpg)
             FROM carsdata)) TEMP);

--CARS-3

SELECT Max(eightCylinders.mpg) - Min(fourCylinders.mpg) AS "MPG Difference"
FROM
  (SELECT mpg
   FROM carsdata
   WHERE cylinders = 8) eightCylinders,

  (SELECT mpg
   FROM carsdata
   WHERE cylinders = 4) fourCylinders;

--INN-1

SELECT r.name,
       r.room,
       Sum(Datediff(checkout, checkin)) AS "Numdays"
FROM reservations res,
     rooms r
WHERE res.room = r.room
GROUP BY res.room
HAVING numdays =
  (SELECT Max(numdays)
   FROM
     (SELECT Sum(Datediff(checkout, checkin)) AS "Numdays"
      FROM reservations
      GROUP BY room) TEMP);

--INN-2

SELECT temp.room,
       rooms.name,
       IF(temp.room IN
            (SELECT room
             FROM reservations
             WHERE checkin <= '2010-07-04'
               AND checkout > '2010-07-04'
             GROUP BY room), 'Occupied', 'Empty') AS "July 4"
FROM
  (SELECT room
   FROM reservations
   GROUP BY room) TEMP,
                  rooms
WHERE temp.room = rooms.room;

--INN-3

SELECT Month(checkin) AS "Month",
       Count(*) AS "NumReservations",
       Sum(Datediff(checkout, checkin) * rate) AS "Revenue"
FROM reservations
GROUP BY Month(checkin)
HAVING revenue =
  (SELECT Max(revenue)
   FROM
     (SELECT Sum(Datediff(checkout, checkin) * rate) AS "Revenue"
      FROM reservations
      GROUP BY Month(checkin)) TEMP);

--MARATHON-1

SELECT state
FROM
  (SELECT Max(num),
          state
   FROM
     (SELECT state,
             Count(*) AS "Num"
      FROM marathon
      GROUP BY state) states) singleState;

--MARATHON-2

SELECT allGroups.state,
       allGroups.sex,
       allGroups.grp,
       allGroups.numrunners
FROM
  (SELECT state,
          sex,
          grp,
          Count(*) AS numRunners
   FROM marathon
   GROUP BY sex,
            grp) allGroups,

  (SELECT Max(allGroups.numrunners) AS maximum,
          state
   FROM
     (SELECT state,
             sex,
             grp,
             Count(*) AS numRunners
      FROM marathon
      GROUP BY state,
               sex,
               grp) allGroups
   GROUP BY allGroups.state) maxes
WHERE maxes.maximum = allGroups.numrunners
GROUP BY allGroups.state,
         allGroups.sex,
         allGroups.grp
ORDER BY allGroups.state;

--MARATHON-3

SELECT place,
       FIRST,
       LAST
FROM marathon m
WHERE sex = 'F'
  AND 29 =
    (SELECT Count(*)
     FROM marathon
     WHERE sex = 'F'
       AND place < m.place);
