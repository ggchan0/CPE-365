# Atticus Liu / aliu44@calpoly.edu
# 4/12/17
# CPE 365-01

import csv

f = open('KATZENJAMMER-build-Songs.sql', 'w')

with open('Songs.csv', 'rb') as csvfile:
   listreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
   next(listreader, None)
   for row in listreader:
      #print >> f, row[0].split(",'")
      print >> f, 'INSERT INTO songs VALUES(' + ' '.join(row) + ');'
      #print >> f, '%s%s%s%s%s%s' % ('INSERT INTO songs VALUES(', row[0], ', ', row[1], ',', ');')


#print >> f, '%s%s%s%s%s%s%s' % ('INSERT INTO receipts VALUES(',
#                  row[0], 'STR_TO_DATE(', row[1],
#                  ''''%d-%M-%Y'),''', row[2], ');')
f.close()
