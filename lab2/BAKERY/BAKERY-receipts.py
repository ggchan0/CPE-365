# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('BAKERY-build-receipts.sql', 'w')

with open('receipts.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      print >> f, '%s%s%s%s%s%s%s' % ('INSERT INTO receipts VALUES(',
                  row[0], 'STR_TO_DATE(', row[1],
                  ''''%d-%M-%Y'),''', row[2], ');')

f.close()
