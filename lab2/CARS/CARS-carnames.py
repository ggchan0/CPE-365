# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('CARS-build-carnames.sql', 'w')

with open('car-names.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO carNames VALUES(' + ' '.join(row) + ');'

f.close()
