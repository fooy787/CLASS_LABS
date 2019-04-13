import pygame
import math3d
import math
import physics2d

class Player(physics2d.PBody):
    def __init__(self, pos, rad):
        super().__init__(pos, rad, 0)
        self.mConeAngle = math.radians(30)    # The half-angle of the cone.
        self.mConeAngleCos = math.cos(self.mConeAngle)
        self.mFaceDir = math3d.VectorN((1,0))
        self.mConeSize = 150
        self.mInvulnerableTimer = 3.0
        self.mHP = 100
        self.mStamina = 100
        # From http://spritedatabase.net/file/496
        self.mImg = pygame.image.load("indiana_jones.bmp")  # sprites are 29 x 34 pixels
        self.mImg.set_colorkey((65,136,164))
        self.mFrame = 0
        self.mAnimTimer = 0.0
        self.mAnimDelay = 0.2
        self.mShoutForce = 30000000
        self.mShouting = False
        self.mShoutSound = pygame.mixer.Sound("ffield.wav")
        self.mShoutSoundPlaying = False


    def renderCone(self, surf):
        if not self.mShouting:
            return

        if True:
            # Draw as a "gradient"
            slices = 20
            cur_angle = self.mRadians - self.mConeAngle
            ang_inc = self.mConeAngle * 2 / slices
            a = self.mConeSize * math3d.VectorN((math.cos(cur_angle), -math.sin(cur_angle)))
            for i in range(slices):
                cur_angle += ang_inc
                b = self.mConeSize * math3d.VectorN((math.cos(cur_angle), -math.sin(cur_angle)))
                power = int(64 * (1.0 - abs(cur_angle - self.mRadians) / self.mConeAngle)) + 32
                pygame.draw.polygon(surf, (power,power,0), (self.mPos.iTuple(), (a + self.mPos).iTuple(), (b + self.mPos).iTuple()))
                a = b
        else:
            # Draw an outline
            a = self.mPos + self.mConeSize * math3d.VectorN((math.cos(self.mRadians - self.mConeAngle), -math.sin(self.mRadians - self.mConeAngle)))
            b = self.mPos + self.mConeSize * math3d.VectorN((math.cos(self.mRadians + self.mConeAngle), -math.sin(self.mRadians + self.mConeAngle)))
            pygame.draw.arc(surf, (255,255,255), (self.mPos[0] - self.mConeSize, self.mPos[1] - self.mConeSize, self.mConeSize * 2, self.mConeSize * 2), \
                                   self.mRadians - self.mConeAngle, self.mRadians + self.mConeAngle)
            pygame.draw.line(surf, (255,255,255), self.mPos.iTuple(), a.iTuple())
            pygame.draw.line(surf, (255,255,255), self.mPos.iTuple(), b.iTuple())


    def render(self, surf):
        # Sprite
        pygame.draw.circle(surf, (255,255,255), self.mPos.iTuple(), self.mRadius, 1)
        d = math.degrees(self.mRadians) % 360
        if d > 315 or d < 45:       group = 0
        elif d >= 45 and d < 135:   group = 1
        elif d >= 135 and d < 224:  group = 2
        else:                       group = 3

        x = group * 3 + self.mFrame
        if self.mInvulnerableTimer > 0:
            self.mImg.set_alpha(128)
        else:
            self.mImg.set_alpha(255)
        surf.blit(self.mImg, (self.mPos[0] - 14, self.mPos[1] - 30), (x * 29, 0, 29, 34))

        # HP gauge
        pygame.draw.rect(surf, (255,255,255), (5,5,104,7), 1)
        if self.mInvulnerableTimer > 0:
            pygame.draw.rect(surf, (128,128,128), (7,7,self.mHP,3))
        else:
            pygame.draw.rect(surf, (255,0,0), (7,7,self.mHP,3))

        # Stamina gauge
        pygame.draw.rect(surf, (255,255,255), (5,13,104,7), 1)
        pygame.draw.rect(surf, (100,100,255), (7,15,self.mStamina,3))

    def pointTowards(self, p):
        self.mRadians = math.atan2(-(p[1] - self.mPos[1]), p[0] - self.mPos[0])

    def update(self, dt):
        # Get the mouse button state
        mpressed = pygame.mouse.get_pressed()

        # Physics-related updates
        super().update(dt)
        self.mFaceDir = math3d.VectorN((math.cos(self.mRadians), -math.sin(self.mRadians)))
        if mpressed[0]:
            self.applyAcceleration(600 * self.mFaceDir, dt)
        self.applyFriction(400, dt)


        # Update the animation
        if self.mVel.magnitudeSquared() > 2:
            self.mAnimTimer += dt
            if self.mAnimTimer >= self.mAnimDelay:
                self.mFrame = (self.mFrame + 1) % 3
                self.mAnimTimer -= self.mAnimDelay

        # Update the invulnerability status
        if self.mInvulnerableTimer > 0:
            self.mInvulnerableTimer -= dt

        # Update the shouting status
        if mpressed[2]:
            self.mShouting = True
            self.mStamina = max(self.mStamina - 60 * dt, 0)
            if self.mStamina <= 0.0:
                self.mShouting = False
            elif self.mShoutSoundPlaying == False:
                self.mShoutSound.play(-1)
                self.mShoutSoundPlaying = True
        else:
            if self.mShoutSoundPlaying:
                self.mShoutSound.stop()
                self.mShoutSoundPlaying = False
            self.mShouting = False
            if self.mStamina < 100:
                self.mStamina = min(self.mStamina + 20 * dt, 100)


    def applyPain(self):
        if self.mInvulnerableTimer <= 0.0:
            self.mHP = max(self.mHP - 10, 0)
            self.mInvulnerableTimer = 2.0

    def calculateShoutForce(self, obj):
        if self.mShouting:
            dir = obj.mPos - self.mPos
            dist = dir.magnitude()
            dirHat = dir / dist
            shoutStr = self.mFaceDir.dot(dirHat)

            if shoutStr > 0 and dist < self.mConeSize:
                return dirHat * shoutStr * self.mShoutForce

        return None






