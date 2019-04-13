#Chase Williams
#Bit reverse without table
import time


try:
    name = input("Please Input  file name: ")
    inFile = open(name, "rb")
except IOError:
    print("File could not be opened")
    exit()

outName = input("Please Input an output name: ")
outFile = open(outName, "wb")
startTime = time.time()
inBytes =  bytearray(inFile.read())

for i in range(0,len(inBytes)):
    y = 0
    tempByte = inBytes[i]
    for j in range(8):
        y = (y << 1) | (tempByte & 1)
        tempByte >>= 1
    inBytes[i] = y

outFile.seek(0)
outFile.write(inBytes)

inFile.close()
outFile.close()

endTime = time.time()

print(str(endTime - startTime) + " seconds")
input()
