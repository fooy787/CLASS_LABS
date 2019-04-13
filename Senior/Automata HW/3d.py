#Chase Williams
#Part 3 D
def S():
    if input_string[ch_index] =='a':
        match('a')
        match('a')
        B()

def A():
    if input_string[ch_index]=='b':
        match('b')
        B()
        match('b')
    else:
        return

def B():
    A()
    match('a')
      

def match(char_to_match):
    global error_count
    global ch_index
    if char_to_match!=input_string[ch_index]:
        print("Error: expected character",char_to_match,"and got",input_string[ch_index])
        error_count+=1

    if ch_index<len(input_string)-1:
        ch_index+=1

input_string=input("enter a string:")+chr(0)

ch_index=0
error_count=0

S()

if error_count==0:
    print("string accepted")
else:
    print("string rejected")



