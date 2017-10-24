# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('KATZENJAMMER-build-Vocals.sql', 'w')

with open('Vocals.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO vocals VALUES(' + ' '.join(row) + ');'

f.close()
