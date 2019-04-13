import math3d
import physics3d
import pygame
import math
import random

class Cannon(physics3d.PBody):
    def __init__(self, initX, img, imgBase):
        super().__init__(math3d.VectorN((initX,500)), 10)
        self.mImg = img
        self.mBase = imgBase
        self.mBullets = []

    def render(self, surf):
        for b in self.mBullets:
            b.render(surf)
        temps = pygame.transform.rotate(self.mImg, math.degrees(self.mRadians))
        surf.blit(temps, self.mPos - math3d.VectorN(temps.get_size()) / 2)
        surf.blit(self.mBase, (int(self.mPos[0] - 95), int(self.mPos[1] + 5)))

    def rotate(self, dRadians):
        self.setRotation(self.mRadians + dRadians)

    def setRotation(self, radians):
        self.mRadians = radians
        if self.mRadians < 0:           self.mRadians = 0
        if self.mRadians > math.pi / 4: self.mRadians = math.pi / 4

    def pointTowards(self, target):
        v = math3d.VectorN((target[0] - self.mPos[0], target[1] - self.mPos[1]))
        self.setRotation(math.atan2(-v[1], v[0]))

    def fire(self, ballImg):
        direction = math3d.VectorN((math.cos(self.mRadians), -math.sin(self.mRadians))) # Unit-length
        muzzlePos = self.mPos + (self.mImg.get_width() / 2) * direction
        muzzleSpeed = random.uniform(350,700) * direction
        self.mBullets.append(CannonBall(muzzlePos, muzzleSpeed, ballImg))

    def update(self, dt):
        super().update(dt)
        for b in self.mBullets:
            b.update(dt)

class CannonBall(physics3d.PBody):
    def __init__(self, initPos, initVel, img):
        super().__init__(initPos, 10, 4.0, initVel)
        self.mImg = img
        self.mRotationSpeed = random.uniform(0, 2 * math.pi)

    def update(self, dt):
        self.applyForce(math3d.VectorN((0, 2000)), dt)
        super().update(dt)
        self.mRadians += self.mRotationSpeed * dt

    def render(self, surf):
        temps = pygame.transform.rotate(self.mImg, math.degrees(self.mRadians))
        surf.blit(temps, (self.mPos[0] - temps.get_width() / 2, self.mPos[1] - temps.get_height() / 2))

# Pygame setup
pygame.display.init()
screen = pygame.display.set_mode((1200,600))
clock = pygame.time.Clock()
done = False
bulletImg = pygame.image.load("coconut.png")
barrelImg = pygame.image.load("barrell.png")
baseImg = pygame.image.load("cannon.png")
C = Cannon(400, barrelImg, baseImg)

# Game Loop
while not done:
    # Update
    dt = clock.tick() / 1000.0
    C.applyFriction(300.0, dt)
    C.update(dt)

    # @@@@@@@@@@@@@@@@@@@
    # @ INPUT           @
    # @@@@@@@@@@@@@@@@@@@
    # ... events
    eList = pygame.event.get()
    for e in eList:
        if e.type == pygame.QUIT:
            done = True
        if e.type == pygame.KEYDOWN and e.key == pygame.K_SPACE or \
           e.type == pygame.MOUSEBUTTONDOWN and e.button == 1:
            C.fire(bulletImg)
    # ... keyboard
    keys = pygame.key.get_pressed()
    if keys[pygame.K_ESCAPE]:
        done = True
    if keys[pygame.K_UP]:
        C.rotate(math.radians(90 * dt))
    if keys[pygame.K_DOWN]:
        C.rotate(math.radians(-90 * dt))
    if keys[pygame.K_LEFT]:
        C.applyForce(math3d.VectorN((-500,0)), dt)
    if keys[pygame.K_RIGHT]:
        C.applyForce(math3d.VectorN((500,0)), dt)
    # ... mouse-related
    C.pointTowards(pygame.mouse.get_pos())

    # Draw
    screen.fill((255,255,255))
    C.render(screen)
    pygame.display.flip()


# Shutdown
pygame.display.quit()
