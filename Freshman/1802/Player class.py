## Player class
import math3d
import pygame
import math
import time

class Player(object):
    def __init__(self, pos):
        self.mVel = math3d.VecotN((0,0,0))
        self.mPos = pos
        self.mHealth = 100

        self.imgList = []
        self.startString = "sprites/player/bob"
        self.iValue = 0
        for i in range[0:9]:
            self.iValue = i
            self.NextString = str(self.iValue)
            self.LoadString = self.startString + self.NextString
            self.TestImg = pygame.image.load(self.LoadString)
            self.imgList.append(self.TestImg)
                

    def ApplyAcceleration(self, a, dt):
        self.mVel += a * dt

    def Friction(self, accelMag, dt):
        if self.mVel.magnitude() > 0:
            self.OldVel = self.mVel.copy()
            F =-self.mVel.normalized_copy() * accelMag
            self.applyAcceleration(F, dt)
            for i in range(oldVel.mDim):
                if oldVel[i] < 0 and self.mVel[i] >= 0 or oldVel[i] >= 0 and self.mVel[i] < 0:
                    self.mVel = math3d.VectorN(oldVel.mDim)
                    break

    def update(self, dt):
        self.mPos += self.mVel * dt
        if self.mVel[0] > 0:
            #Moving Right Images
        if self.mVel[0] < 0:
            #Moving Left Images
        if self.mVel[1] > 0:
            #Moving Down Images
        if self.mVel[1] < 0:
            #Moving Up images

            
    def render(self, surf):
        surf.blit(img, (self.mPos[0] - img.get_width() / 2, self.mPos[1] - img.get_height() / 2))
    
