import csv
import sys
import random
import argparse
from pathlib import Path

def reset(percent=50):
    return random.randrange(100) < percent

parser = argparse.ArgumentParser(description='Create random elements for database')
parser.add_argument('integers', metavar='N', type=int,
                    help='Number of elements to generate')
parser.add_argument('--debug', action='store_true',
                    help='display generated elements')

parser.add_argument('--genSearch', action='store_true',
                    help='generates search files')

args = parser.parse_args()

random.seed(78965)

MAX_BOUNDRY = 99999999
# try:
nbrOfElements = args.integers
if (nbrOfElements > MAX_BOUNDRY):
	print("invalid array size")

outputCsv = Path().absolute().parent
outputCsv = outputCsv / 'testCases'
outputCsv = outputCsv / ('Users_' + sys.argv[1])

# Random generation of IDs
try:
	randIDs = random.sample(range(1, MAX_BOUNDRY), nbrOfElements)
	randSalaries = random.sample(range(1, MAX_BOUNDRY), nbrOfElements)
except ValueError:
	print('Sample size exceeded population size.')

with open("babyNames.txt", "r") as ins:
	babyNames = []
	for line in ins:
		babyNames.append(line)

if args.debug:
	print(randIDs)

# Create List holding elements of the first row
attributes = []
attributes.append('id')
attributes.append('employee')
attributes.append('salary')

operation = []
operation.append('>')
operation.append('=')
operation.append('<')

# operation.append('>=')
# operation.append('<=')

# Genereate random elements for the remainder of the attributes
rows = []
for i in range(nbrOfElements):
	row = []
	row.append(randIDs[i])
	row.append(random.choice(babyNames).strip('\n'))
	row.append(random.choice(randSalaries))
	if args.debug:
		print(row)
	rows.append(row)

with open(str(outputCsv)+'.csv', 'w', newline='') as csvfile:
	filewriter = csv.writer(csvfile, delimiter=',', quotechar='|', quoting=csv.QUOTE_MINIMAL)
	filewriter.writerow(attributes)
	for row in rows:
		filewriter.writerow(row)

with open(str(outputCsv)+'_queries_dynamo.csv', 'w', newline='') as csvfile:
	filewriter = csv.writer(csvfile, delimiter=',', quotechar='|', quoting=csv.QUOTE_MINIMAL)
	for row in rows:
		if(reset(nbrOfElements/50)):
			newLine = []
			newLine.append('id')
			newLine.append('=')
			newLine.append(row[0])
			filewriter.writerow(newLine)

with open(str(outputCsv)+'_queries_manual.csv', 'w', newline='') as csvfile:
	filewriter = csv.writer(csvfile, delimiter=',', quotechar='|', quoting=csv.QUOTE_MINIMAL)
	for row in rows:
		if(reset(nbrOfElements/50)):
			newLine = []
			attribute = random.choice(attributes)
			newLine.append(attribute.upper())
			newLine.append('=')
			newLine.append(row[attributes.index(attribute)])
			filewriter.writerow(newLine)

with open(str(outputCsv)+'_scan.csv', 'w', newline='') as csvfile:
	filewriter = csv.writer(csvfile, delimiter=',', quotechar='|', quoting=csv.QUOTE_MINIMAL)
	for row in rows:
		if(reset(nbrOfElements/50)):
			newLine = []
			attribute = random.choice(attributes)
			newLine.append(attribute.upper())
			newLine.append(random.choice(operation))
			newLine.append(row[attributes.index(attribute)])
			filewriter.writerow(newLine)

csvfile.close()