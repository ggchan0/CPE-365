# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('KATZENJAMMER-build-Albums.sql', 'w')

with open('Albums.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO albums VALUES(' + ' '.join(row) + ');'

f.close()
