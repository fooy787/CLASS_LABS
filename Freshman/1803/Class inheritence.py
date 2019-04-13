class Animal(object):
    def __init__(self, name):
        self.mName = name

    def talk(self):
        print("Hi. I'm ", self.mName)


class Dog(Animal):
    def __init__(self, name, breed):
        super().__init__(name)
        self.mBreed = breed
        
        
    def talk(self):
        print("Hi, I'm a dog and my name is", self.mName)

    def bark(self):
        print("bark")

a = Animal("fasoi")
a.talk()

b = Dog("Fido", "spaniel")
b.talk()
b.bark()
print(b.mBreed)
