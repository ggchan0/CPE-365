# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('BAKERY-build-goods.sql', 'w')

with open('goods.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, 'INSERT INTO goods VALUES(' + ' '.join(row) + ');'

f.close()
