pDay = 23    #or whatever is today's date
pMonth = 11  #or whatever is this month
pYear = 2005 #or whatever is this year

def printAgeHistory (bDay, bMonth, bYear):
    year = bYear + 1
    age = 1

    print("This person was born on " + str(bDay) + "/" + str(bMonth) + "/" + str(bYear))
    while year < pYear:
          print("This person was " + str(age) + " on " + str(bDay) + "/" + str(bMonth) + "/" + str(year))
          year = year + 1
          age = age + 1
    if (bMonth == pMonth):
        print("This person is " + str(age) + " today!")
    else:
        print("This person will be " + str(age) + " on " + str(bDay) + "/" + str(bMonth) + "/" + str(year))

def main():
    printAgeHistory(pDay, pMonth, 2000)
    print("Another person")
    printAgeHistory(13, 11, 2000)

if __name__ == '__main__':
    main()

