import math3d
import pygame
import random
import math

class Particle(object):
    def __init__(self, x, y, vx, vy, rad):
        """ (x, y) is the starting position (in pixels)
            (vx, vy) is the initial velocity vector (where vx
                       is the horiz speed (in px / s) and vy
                       is the vert. speed (in px / s) """
        self.mPos = math3d.VectorN((x, y))
        self.mVel = math3d.VectorN((vx, vy))
        self.mRad = rad
        self.mColor = (random.randint(100,255), random.randint(100,255),\
                       random.randint(100,255))
        self.mMass = 0.1 * math.pi * (rad ** 2)

    def update(self, dt):
        self.mPos += self.mVel * dt

    def render(self, surf):
        pygame.draw.circle(surf, self.mColor, self.mPos.iTuple(), self.mRad)

    def applyAcceleration(self, a, dt):
        self.mVel += a * dt

    def applyForce(self, F, dt):
        a = F / self.mMass
        self.applyAcceleration(a, dt)

# Pygame setup
pygame.display.init()
screen = pygame.display.set_mode((800,600))
clock = pygame.time.Clock()
done = False

PList = []
for p in range(100):
    P = Particle(400, 300, random.uniform(-300,300), \
                 random.uniform(-300,300), random.randint(5, 35))
    PList.append(P)

# Game Loop
while not done:
    # Updates
    dt = clock.tick() / 2000.0
    for P in PList:
        P.update(dt)
        # Apply gravity
        P.applyAcceleration(math3d.VectorN((0,400)), dt)
        #P.applyForce(math3d.VectorN((0,400)) * P.mMass, dt)
        # Bounce off walls
        if P.mPos[0] < P.mRad or P.mPos[0] > 800 - P.mRad:
            P.mVel[0] = -P.mVel[0]
        if P.mPos[1] < P.mRad or P.mPos[1] > 600 - P.mRad:
            P.mVel[1] = -P.mVel[1]


    # Input
    eList = pygame.event.get()
    # ... event-handling
    for e in eList:
        if e.type == pygame.QUIT or (e.type == pygame.KEYDOWN and \
                                     e.key == pygame.K_ESCAPE):
            done = True
    # ... device-polling
    keys = pygame.key.get_pressed()
    mbut = pygame.mouse.get_pressed()
    mpos = math3d.VectorN(pygame.mouse.get_pos())
    if mbut[0]:
        for P in PList:
            accel = (mpos - P.mPos).normalized_copy() * 30000
            P.applyForce(accel, dt)

    # Drawing
    screen.fill((0,0,0))
    for P in PList:
        P.render(screen)
    pygame.display.flip()
    print(int(clock.get_fps()))
# Pygame shutdown
pygame.display.quit()
