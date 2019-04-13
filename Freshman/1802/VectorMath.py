import pygame
import math3d
import random

class Particle(object):
    def __init__(self, x, y):
        self.mPos = math3d.VectorN((x, y))
        self.mVel = math3d.VectorN((random.uniform(-500,500),\
                                    random.uniform(-500,500)))

        self.mRad = random.randint(5,25)
        self.mColor 
    def update(self, dt):
        self.mPos += self.mPos * dt

    def render(self, surf):
        pygame.draw.circle(surf, (255, 255, 255), self.mPos.iTuple(), 5)

    def applyAcceleration(self, a, dt):
        self.mVel += a * dt
    
pygame.init()

screen = pygame.display.set_mode((800,600))
clock = pygame.time.Clock()
done = False
P = Particle(400, 300)

while not done:
    dt = clock.tick() / 1000.0
    P.applyAcceleration(math3d.VectorN((0,200)), dt)
    P.update(dt)
    eList = pygame.event.get()
    for e in eList:
        pass

    keys = pygame.key.get_pressed()
    if keys[pygame.K_ESCAPE]:
        done = True
    mbut = pygame.mouse.get_pressed()
    mpos = pygame.mouse.get_pos()
    if mbut[0]:
        a = (math3d.VectorN(mpos) - P.mPos).normalized_copy() * 600
        P.applyAcceleration(a, dt)
    screen.fill((0,0,0))
    P.render(screen)
    pygame.display.flip()


pygame.quit()
