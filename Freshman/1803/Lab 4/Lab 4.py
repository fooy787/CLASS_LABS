#Chase Williams
#Lab 4
import pygame
import math
import time
import math3d

class Curve(object):
    def __init__(self):
        self.CPList = [] #Add main points
        self.TList = [] #Tangent list
        self.DrawList = []
        self.Resolution = 10 #distance between points

    def addCP(self, p):
        self.CPList.append(p)
#        self.TList.append()#some random value for this number)
#        self.genDraw()

    def genDraw(self): #Generates lines to draw in between control points
        for i in self.TList:
            self.p0 = self.CPList[i]
            self.p1 = self.CPList[i + 1]
            self.t0 = i
            self.t1 = i + 1
            u = 0
            for j in range(self.resolution): #Calculates tangent
                a = 2(u ** 3) - 3(u ** 2) + 1
                b = u ** 3 - 2(u ** 2) + u
                c = -2(u ** 3) + 3(u ** 2)
                d = u ** 3 - u ** 2
                p = a * self.p0 + b * self.t0 + c * self.p1 + d * self.t1
                self.DrawList.append(p)
                u += 1 / (resolution + 1)

    def Render(self): #renders objects to the screen
        pygame.draw.circle(window, (255, 255, 255), self.CPList[-1], 10)
        if len(self.CPList) > 1:
            for i in range(self.Resolution):
                pygame.draw.lines(window, (255, 0, 0), False, self.CPList)

pygame.init()

window = pygame.display.set_mode((800,600))
CP = Curve()
done = False
while not done:
    #mBut = pygame.mouse.get_pressed()
    mPos = math3d.VectorN((pygame.mouse.get_pos()))
    evtList = pygame.event.get()
    for evt in evtList:
        if evt.type == pygame.QUIT:
            done = True
    if evt.type == pygame.MOUSEBUTTONDOWN: #when mouse is clicked, add mouse pos to CPList
        CP.CPList.append(mPos.iTuple())
        for p in range(CP.Resolution):
            CP.Render()
    #if mBut[0]:
    #    CP.addCP(mPos)
    #    CP.Render()
    #window.fill((0, 0, 0))
    pygame.display.flip()

pygame.display.quit()


