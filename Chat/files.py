print("hello")

fo = open("foo.txt", "r")
print ("Name of the file: ", fo.name)

read_content = fo.read()
print(read_content)

# Close opend file
fo.close()

with open("foo.txt", 'a+') as file2:

    # write contents to the test2.txt file
    file2.write('Programming is Fun.')
    file2.write('Programiz for beginners')
