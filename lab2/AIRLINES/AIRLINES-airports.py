# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('AIRLINES-build-airports.sql', 'w')

with open('airports100.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO airports VALUES(' + ' '.join(row) + ');'

f.close()
