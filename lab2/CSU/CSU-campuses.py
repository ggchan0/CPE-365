# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('CSU-build-Campuses.sql', 'w')

with open('Campuses.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO campuses VALUES(' + ' '.join(row) + ');'

f.close()
