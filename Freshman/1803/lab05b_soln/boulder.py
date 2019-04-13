import math3d
import random
import physics2d
import math
import pygame

class Boulder(physics2d.PBody):
    # Note: This is what in most language is called a "static attribute".  It's
    # shared by all instances of the class (or viewed another way, it is associated
    # with the class, not the instance).  To access these, use "Boulder.xxx"
    thudSound = None
    thudDelay = 0.4
    thudTimer = 0.0

    def __init__(self, pos, vel, rad):
        super().__init__(pos, rad, 4 / 3 * math.pi * rad ** 3, vel)
        self.mLockOrientationToVelocity = True
        self.mImgSize = int(self.mRadius) * 2
        self.mRadians = random.uniform(-math.pi, math.pi)
        self.mImg = pygame.image.load("boulder.bmp").convert()
        self.mNumFrames = self.mImg.get_width() // 64
        self.mImg = pygame.transform.scale(self.mImg, (self.mImgSize * self.mNumFrames,self.mImgSize))
        self.mImgColorKey = (64,64,64)
        self.mScratchSurf = pygame.Surface((self.mImgSize, self.mImgSize))
        self.mFrame = 0
        self.mFrameDelay = None   # Updated every frame in update
        self.mFrameTimer = 0.0

        # Load one instance of the sound
        if Boulder.thudSound == None:
            Boulder.thudSound = pygame.mixer.Sound("thud.wav")
            Boulder.thudSound.set_volume(0.3)


    def update(self, dt):
        super().update(dt)

        self.applyFriction(50, dt)

        spd = self.mVel.magnitude()
        if spd > 0:
            self.mFrameDelay = (2 * math.pi * self.mRadius) / (self.mNumFrames * spd)
            self.mFrameTimer += dt
            if self.mFrameTimer >= self.mFrameDelay:
                self.mFrame = (self.mFrame + 1) % self.mNumFrames
                self.mFrameTimer -= self.mFrameDelay

	
    def render(self, surf):
        self.mScratchSurf.blit(self.mImg, (0,0), (self.mImgSize * self.mFrame, 0, self.mImgSize, self.mImgSize))
        rimg = pygame.transform.rotate(self.mScratchSurf, math.degrees(self.mRadians) + 90)
        rimg.set_colorkey(self.mImgColorKey)
        surf.blit(rimg, (self.mPos - math3d.VectorN(rimg.get_size()) / 2))

    # Note: This is what's called a "static method" in most classes.  We don't include
    # self because the method is meant to be called like "Boulder.playThud()" instead
    # of "b.playThud()"
    def playThud():
        if Boulder.thudTimer <= 0.0:
            Boulder.thudTimer = Boulder.thudDelay
            Boulder.thudSound.play()

    def updateThud(dt):
        if Boulder.thudTimer > 0:
            Boulder.thudTimer -= dt