#!/usr/bin/python

import sys

if (len(sys.argv) != 2):
    print "Usage: python makeDynUsers.py [numUsers]"
    sys.exit()

numUsers = int(sys.argv[1])

print ("Creating files for {0} users", format(numUsers))

outfile = open('dynamicUsers.csv', 'w')

for i in range(0, numUsers):
    outfile.write("DynUser")
    outfile.write(str(i))
    outfile.write("\n")

outfile.close()

outfile = open('dynamicAttributes.txt', 'w')

outfile.write('POST /attribute {"name" : "test-attrib1"}')
outfile.write('\n')

outfile.write('POST /subscriber-attribute {')
outfile.write('\n')

outfile.write('"add": [')
outfile.write('\n')

# {"subscriber": "subscriber-1.0", "name":"attribute-1.2", "value":"value-1.0.2"}
for i in range(0, numUsers):
    outfile.write('{"subscriber": "')
    outfile.write('DynUser')
    outfile.write(str(i))
    outfile.write('", "name":"test-attrib1", "value":"yes"}')
    if (i < numUsers - 1):
        outfile.write(',')
    outfile.write("\n")


outfile.write(']}')
outfile.write('\n')
outfile.close()
