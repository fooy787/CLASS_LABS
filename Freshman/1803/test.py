import pygame
import math3d

pygame.init()

screen = pygame.display.set_mode((800,600))
clock = pygame.time.Clock()
done = False

circlePos = math3d.VectorN(400, 300)   #This is a point
circleVel = math3d.VectorN(100, 0) #This is a vector



gravAccel = math3d.VectorN(0,50)


while not done:
    dt = clock.tick(60) / 1000.0

    circlePos += circleVel * dt

    circleSpeed = 200 #Number of pixels to move per second

    circleVel = mouseDirection.normalize() * circleSpeed
    
    event = pygame.event.poll()

    mx, my = pygame.mouse.get_pos()

    mousePos = math3d.VectorN(mx, my)

    mouseDir = mousePos - circlePos

    mouseAngle = math.atan2(-mouseDir[1], mouseDir[0])
    keys = pygame.key.get_pressed()
    if keys[pygame.K_ESCAPE]:
        done = True

    

    screen.fill((0,0,0))

    pygame.draw.circle(screen,(255,0,0), circlePos.int(), 30)
    tip = circlePos + 50 *circleVel.normalized()
    perpVector = math3d.VectorN(-circleVel[1], circleVel[0]).normalized()
    sidePt = circlePoint + 30 * perpVector
    
    pygame.draw.line

    pygame.display.flip()

pygame.quit()
