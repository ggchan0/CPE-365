# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('KATZENJAMMER-build-Instruments.sql', 'w')

with open('Instruments.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO instruments VALUES(' + ' '.join(row) + ');'

f.close()
