import pygame
from math3d import *
from objects3d import *
import raytracer


def adjustSetup(RT, fov, near):
    RT.setCamera(VectorN((0,3,-60)), VectorN((0,0,0)), VectorN((0, 1, 0)), fov, near)



# Pygame setup
pygame.display.init()
screen = pygame.display.set_mode((300,200))
done = False
nears = [0.1, 1, 5, 15]
fovs = [30, 60, 90]
currentLine = 0
currentFov = 0
currentNear = 0

# Raytracer setup
RT = raytracer.Raytracer(screen)
RT.mObjects.append(Plane(VectorN((0,1,0)), 0, Material(VectorN((1,1,0)))))
RT.mObjects.append(Sphere(VectorN((0,0,0)), 10, Material(VectorN((1,0,0)))))
RT.mObjects.append(AABB(VectorN((25,5,0)), VectorN((40,25,20)), Material(VectorN((0,1,0)))))
RT.mObjects.append(CylinderY(VectorN((-17,6,30)), 22.0, 15.0, Material(VectorN((0.7,0.7,1)))))
adjustSetup(RT, fovs[currentFov], nears[currentNear])

while not done:
    # Input
    eList = pygame.event.get()
    for e in eList:
        if e.type == pygame.QUIT or (e.type == pygame.KEYDOWN and e.key == pygame.K_ESCAPE):
            done = True

    # Draw
    if currentLine < screen.get_height():
        RT.renderOneLine(currentLine)
        currentLine += 1
    else:
        currentLine = 0
        pygame.image.save(screen, "output_" + str(nears[currentNear]).replace(".", "_") + "__" + str(fovs[currentFov]) + ".bmp")
        currentNear += 1
        if currentNear >= len(nears):
            currentNear = 0
            currentFov += 1
            if currentFov >= len(fovs):
                done = True
        if currentFov < len(fovs) and currentNear < len(nears):
            screen.fill((0,0,0))
            adjustSetup(RT, fovs[currentFov], nears[currentNear])
    pygame.display.flip()

pygame.display.quit()
