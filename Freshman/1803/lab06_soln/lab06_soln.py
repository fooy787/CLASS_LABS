# Test Program for Lab05.  You should NOT change anything here EXCEPT the little bit in the
# game loop to adjust the ray placement (this spot is marked with comments).  You should define objects3d
# and all of the contained methods to make this test program work.  You may find it helpful
# to comment primitives out and un-comment as you add them to objects3d.  Note: these primitives
# are in 3D.  Your 2D pygameRender methods should ignore the z component, but it is very
# important that your hit detection work in 3d.
import pygame
from objects3d import *
from math3d import *

# Pygame setup
pygame.display.init()
pygame.font.init()
screen = pygame.display.set_mode((800,600))
font = pygame.font.SysFont("Courier New", 15)
done = False

# Scene Setup
# ... create some "helper" variables
redMat = Material(VectorN((1,0,0)))
greenMat = Material(VectorN((0,1,0)))
blueMat = Material(VectorN((0,0,1)))
yellowMat = Material(VectorN((1,1,0)))
purpleMat = Material(VectorN((1,0,1)))
aquaMat = Material(VectorN((0,1,1)))
pinkMat = Material(VectorN((1,0.7,0)))
rayOrigin = VectorN((400,300,0))
rayDir = VectorN((-4,-3,0))
sphereCenter = VectorN((100,100,0))
plane1Normal = VectorN((1,3,0))
plane1D = 600
plane2Normal = VectorN((0,1,0))
plane2D = 550
plane3Normal = VectorN((1,0,0))
plane3D = 750
cylinderBase = VectorN((500,250,0))
cylinderHeight = 150
cylinderRad = 60
boxPtA = VectorN((200,400,0))
boxPtB = VectorN((100,480,0))
# ... actually construct the scene by adding elements to the Scene list
#     (as you're writing this lab, you may want to comment out these
#      lines until that primitive is ready for testing)
Shapes = []
Shapes.append((Ray(rayOrigin, rayDir), "ray"))  # Note: Shapes[0] is always the ray...
Shapes.append((Sphere(sphereCenter, 75, redMat), "sphere"))
Shapes.append((Plane(plane1Normal, plane1D, blueMat), "plane1"))
Shapes.append((Plane(plane2Normal, plane2D, aquaMat), "plane2"))
Shapes.append((Plane(plane3Normal, plane3D, purpleMat), "plane3"))
Shapes.append((AABB(boxPtA, boxPtB, pinkMat), "aabb"))
Shapes.append((CylinderY(cylinderBase, cylinderHeight, cylinderRad, yellowMat), "cylinder"))

# Game Loop
while not done:
    # Update (Determine all intersection points (Vector3's), and append them
    #         to a list of 2D tuples that will be used in the draw section)
    intPoints = []
    intColors = []
    for i in range(1, len(Shapes)):
        result = Shapes[i][0].rayHit(Shapes[0][0])
        if result:
            for p in result.mIntersectionPoints:
                intPoints.append(p.iTuple()[0:2])
                intColors.append(result.mHitObject.mMaterial.getPygameColor())

    # Input (I'm only using "device-polling" here)
    pygame.event.get()
    keys = pygame.key.get_pressed()
    mbut = pygame.mouse.get_pressed()
    mpos = pygame.mouse.get_pos()
    if keys[pygame.K_ESCAPE]:
        done = True
    if mbut[0]:
        # FINISH ME!!!
        # Make the ray's origin "snap" to the mouse position
        pass
        Shapes[0][0].mOrigin = VectorN((mpos[0], mpos[1], 0))
    elif mbut[2]:
        # FINISH ME!!
        # Make the ray point towards the mouse *IF* the mouse
        # is a few pixels away from the ray origin
        pass
        direction = VectorN((mpos[0], mpos[1], 0)) - Shapes[0][0].mOrigin
        if direction.magnitudeSquared() > 3:
            Shapes[0][0].mDirection = direction.normalized_copy()

    # Draw
    screen.fill((0,0,0))
    for s in Shapes:
        s[0].pygameRender(screen, s[1], font)
    for i in range(len(intPoints)):
        try:
            # Occasionally (mainly with the plane), we'll get a really far away
            # point that's too big to hold in a C long.  This will break, but
            # the try / except keeps it from crashing.
            pygame.draw.circle(screen, intColors[i], intPoints[i], 6)
        except:
            pass
    pygame.display.flip()

# Pygame shutdown
pygame.display.quit()
pygame.font.quit()
