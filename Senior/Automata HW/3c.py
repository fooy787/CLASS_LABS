#Chase Williams
#Part 3 C
def S():
    if input_string[ch_index]:
        A()
        B()
    else:
        return

def A():
    if input_string[ch_index]=='c':
        match('c')
        B()
    else:
        A()
        match('a')

def B():
    if input_string[ch_index] =='c':
        match('c')
    else:
        B()
        match('b')
        S()
        match('b')
      

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



