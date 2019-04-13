#Chase Williams
#Lab 5

import pygame
import math
import math3d
import physics3d
import random
from random import randint

"""for i in range(len(blist)):
       for j in range(i+1, len(blist)):
           if blist[i] and blist[j] collide:
               """


class Indiana_Jones(object):
    def __init__(self):
        self.Location = math3d.VectorN((400, 300))
        self.mVel = math3d.VectorN((500, 0))
        self.colorkey = pygame.Color(65, 136, 164)
        self.spritesheet = pygame.image.load("indiana_jones.bmp").convert()
        self.spritesheet.set_colorkey(self.colorkey)
        self.pix = 29
        self.sheetx = 0  #Where on the sprite sheet(pix)
        self.sheety = 0  #  ^
        self.lensheetx = 348  #Length of the sprite sheet
        self.lensheety = 45  #Height of the sprite sheet
        self.sprite = []  #List of the sprites
        self.dirspr = []  #Sprite list to render from
        i = 0
        for i in range(0, self.lensheetx):
            if self.lensheetx != self.sheetx:
                self.spritesheet.set_clip(pygame.Rect(self.sheetx, self.sheety, self.pix, self.lensheety))  #Sets location to cut sprites
                sprite = self.spritesheet.subsurface(self.spritesheet.get_clip())  #Cuts sprites
                self.sprite.append(sprite)
                self.sheetx += 29  #Increments by amount of pixels in between each sprite
                i += 1

    def applyAccel(self, dt):
            self.Location += self.mVel * dt
    def mouseAngle(self):
        x = mpos - player.Location
        new = math3d.VectorN(x)
        t = math.atan2(new[1] * -1, new[0])
        s = math.degrees(t)
        return s


    def update(self, b, e):
        #RIGHT
        if b == 0:
            self.dirspr.append(self.sprite[b])
            if mbut[0]:
                for i in range(0, mpos.iTuple()[0] - player.Location.iTuple()[0]):
                    player.Location[0] += i * dt
                    i += 1
                    """If mouse position != i then animate the sprite"""
                    if i != mpos[0]:
                        j = 0
                        for j in range(b, e):
                            player.dirspr.append(player.sprite[j])
                            j += 1
                            if j == e:
                                j = b
        #UP
        if b == 3:
            self.dirspr.append(self.sprite[b])
            if mbut[0]:
                for i in range(0, player.Location.iTuple()[1] - mpos.iTuple()[1]):
                    player.Location[1] -= i * dt
                    i -= 1
                    """If mouse position != i then animate the sprite"""
                    if i != mpos[0]:
                        j = 0
                        for j in range(b, e):
                            player.dirspr.append(player.sprite[j])
                            j += 1
                            if j == e:
                                j = b
        #LEFT
        if b == 6:
            self.dirspr.append(self.sprite[b])
            if mbut[0]:
                for i in range(0, player.Location.iTuple()[0] - mpos.iTuple()[0]):

                    player.Location[0] -= i * dt
                    i -= 1
                    """If mouse position != i then animate the sprite"""
                    if i != mpos[0]:
                        j = 0
                        for j in range(b, e):
                            player.dirspr.append(player.sprite[j])
                            j += 1
                            if j == e:
                                j = b

        #DOWN
        if b == 9:
            self.dirspr.append(self.sprite[b])
            if mbut[0]:
                for i in range(0, mpos.iTuple()[1] - player.Location.iTuple()[1]):
                    player.Location[1] += i * dt
                    i += 1
                    """If mouse position != i then animate the sprite"""
                    if i != mpos[0]:
                        j = 0
                        for j in range(b, e):
                            player.dirspr.append(player.sprite[j])
                            j += 1
                            if j == e:
                                j = b

    def render(self):
        screen.blit(self.dirspr[-1], self.Location)


class Laser(object):
    def __init__(self):
        self.alpha = 0
        self.position = math3d.VectorN((randint(0, swidth), randint(0, sheight)))

    def rotate(self, dt):
        self.alpha += 45 * dt
        Uhat = math3d.VectorN((math.cos(math.radians(self.alpha)), -math.sin(math.radians(self.alpha))))
        return Uhat
    def render(self, dt):
        pygame.draw.circle(screen, (255,0,0), self.position.iTuple(), 5)
        pygame.draw.line(screen, (255,0,0), self.position.iTuple(), (10000 * self.rotate(dt) + self.position).iTuple())

class Meteor(object):
    def __init__(self):

#img = pygame.image.load("meteor.png").convert()
#img.set_colorkey((64,64,64))
        self.MeteorList = []
        self.mListiTuple = []
        #Velocity
        self.MeteorVelocity = math3d.VectorN((random.randint(-100,100), random.randint(-100,100)))
        self.TVelocity = math3d.VectorN((400,400))

        self.Resize = random.uniform(0,1)
        #Resize
        self.img = pygame.image.load("boulder_simple.bmp").convert()
        self.img.set_colorkey((64, 64, 64))
        self.imgX = self.img.get_width()
        self.imgY = self.img.get_height()

        self.ResizeX = int(self.imgX * self.Resize)
        self.ResizeY = int(self.imgY * self.Resize)

        self.Rimg = pygame.transform.scale(self.img,(self.ResizeX, self.ResizeY))
        #Meteor count
        self.maxMeteors = random.randint(1, 50)
        self.startCount = 0
        #Position      
        self.MeteorPosition = math3d.VectorN((random.randint(0+self.ResizeX,800-self.ResizeX),random.randint(0+self.ResizeY,600-self.ResizeY)))
        self.miTuple = self.MeteorPosition.iTuple()
        self.MeteorList.append(self.MeteorPosition)
        self.mListiTuple.append(self.miTuple)

    def render(self, surf, color=(255, 0, 0)):
        for i in self.MeteorList:
#            pygame.draw.circle(surf, color, self.MeteorPosition.iTuple(), self.Radius)
            surf.blit(self.Rimg, self.MeteorPosition.iTuple())                                   
    def ApplyForces(self, dt):
        self.dtime = dt
        #Applies a force to the meteors at a random velocity per meteor
        for i in self.MeteorList:
            self.MeteorPosition += dt * self.MeteorVelocity
#Set up    
pygame.init()
screen = pygame.display.set_mode((800,600))
swidth = 800
sheight = 600
screen = pygame.display.set_mode((swidth, sheight))
player = Indiana_Jones()
laser = Laser()
clock = pygame.time.Clock()
done = False

meteorList = []
for m in range(20):
    M = Meteor()
    meteorList.append(M)
#Game Loop
while not done:
    dt = clock.tick() / 1000.0
    eList = pygame.event.get()
    for e in eList:
        if e.type == pygame.QUIT:
            done = True
    for m in meteorList:
        m.ApplyForces(dt)
        #Bounce off walls
        if m.MeteorPosition[0] < 0 or m.MeteorPosition[0] > 800 - m.ResizeX:
            m.MeteorVelocity[0] = -m.MeteorVelocity[0]
        if m.MeteorPosition[1] < 0 or m.MeteorPosition[1] > 600 - m.ResizeY:
            m.MeteorVelocity[1] = -m.MeteorVelocity[1]
        #Checks for hits
#    for i in range(len(meteorList)):
#        for j in range(i + 1,len(meteorList)):
#            if (((meteorList[i].MeteorPosition[0] - meteorList[j].MeteorPosition[0]) ** 2 (meteorList[i].MeteorPosition[1] - meteorList[j].MeteorPosition[1]) ** 2) ** 0.5) + 10 < 0:
#                i.MeteorVelocity -= 2*(j.MeteorVelocity)
#                j.MeteorVelocity -= 2*(i.MeteorVelocity)        
    keys = pygame.key.get_pressed()
    mbut = pygame.mouse.get_pressed()
    mpos = math3d.VectorN(pygame.mouse.get_pos())

    #RIGHT
    if 0 <= player.mouseAngle() < 45 or 0 > player.mouseAngle() > -45:
        player.update(0, 3)

    #UP
    elif 45 <= player.mouseAngle() < 135:
        player.update(3, 6)

    #LEFT
    elif 135 <= player.mouseAngle() <= 180 or -180 <= player.mouseAngle() <= -135:
        player.update(6, 9)

    #DOWN
    elif -135 < player.mouseAngle() < -45:
        player.update(9, 12)


    screen.fill((0,0,0))
    for m in meteorList:
        m.render(screen)
    laser.render(dt)
    player.render()
    pygame.display.flip()

#Pygame stop
pygame.quit()
