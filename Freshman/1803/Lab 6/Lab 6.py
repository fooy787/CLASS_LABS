#Chase Williams
import pygame
from math3d import *

class Material(object):
    def __init__(self, mDiffuse):  # Passes color as a number from 0 to 1
        self.mDiffuse = mDiffuse

        if not isinstance(mDiffuse, VectorN):  # Checks that Material is a VectorN
                raise TypeError("Material needs to be in a VectorN")
        for i in mDiffuse:  # Checks that mDiffuse is a number from 0 to 1
            if not 0 <= i <= 1:
                raise TypeError("Enter a RGB value from 0 to 1 (0 = 0, 1 = 255)")

    def getPygameColor(self):
        pygameColor = VectorN(self.mDiffuse * 255)
        return pygameColor.iTuple()

class Ray(object):
    def __init__(self, origin, direction):
        self.Origin = origin
        self.Direction = direction

        self.OriginTup = self.Origin.iTuple()

    def pygameRender(self):
        pygame.draw.circle(screen,(255,255,255),(int(self.Origin[0]),int(self.Origin[1])),10)
        pygame.draw.line(screen,(255,255,255),(int(self.Origin[0]),int(self.Origin[1])), (int(self.Direction[0]),int(self.Direction[1])))
            
class Plane(object):
    def __init__(self, vec, d, mat):
        self.Vec = vec
        self.Vector = self.Vec.normalized_copy()
        self.dVal = d
        self.material = mat
        if self.Vector[1] != 0.0 or self.Vector[1] != 0:
            self.A = [0, self.dVal / self.Vector[1]]
            self.B = [800, (self.dVal - 800 * self.Vector[0])/ self.Vector[1]]
        if self.Vector[1] == 0.0 or self.Vector[1] == 0:
            self.A = [self.dVal / self.Vector[0],0]
            self.B = [(self.dVal - 600 * self.Vector[1])/ self.Vector[0],600]
            
    def pygameRender(self):
        pygame.draw.line(screen, self.material.getPygameColor(), self.A, self.B, 3)

    def rauHit(self, r):
        self.Ray = r
        
class AABB(object):
    def __init__(self, pA, pB, material):
        self.pA = pA
        self.pB = pB
        self.Material = material
        if self.pA[0] > self.pB[0]:
            self.pMaxX = self.pA[0]
            self.pMinX = self.pB[0]
        else:
            self.pMaxX = self.pB[0]
            self.pMinX = self.pA[0]

        if self.pA[1] > self.pB[1]:
            self.pMaxY = self.pA[1]
            self.pMinY = self.pB[1]
        else:
            self.pMaxY = self.pB[1]
            self.pMinY = self.pA[1]

        if self.pA[2] > self.pB[2]:
            self.pMaxZ = self.pA[2]
            self.pMinZ = self.pB[2]
        else:
            self.pMaxZ = self.pB[2]
            self.pMinZ = self.pA[2]
        self.pMin = VectorN((self.pMinX, self.pMinY, self.pMinZ))
        self.pMax = VectorN((self.pMaxX, self.pMaxY, self.pMaxZ))


    def pygameRender(self):
        pygame.draw.rect(screen, self.Material.getPygameColor(),(self.pMinX, self.pMinY, (self.pMaxX - self.pMinX),(self.pMaxY - self.pMinY)), 2)
                             
class Sphere(object):
    def __init__(self, center, radius, material):
        self.Center = center
        self.Radius = radius
        self.CenterX = self.Center[0]
        self.CenterY = self.Center[1]
        
        self.Color = material

    def pygameRender(self):
        pygame.draw.circle(screen, self.Color.getPygameColor(), self.Center.iTuple()[0:2], self.Radius, 2)

    def rayHit(self, r):
        pass
        
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
Shapes.append(Ray(rayOrigin, rayDir))  # Note: Shapes[0] is always the ray...
Shapes.append(Plane(plane1Normal, plane1D, blueMat))
Shapes.append(Plane(plane2Normal, plane2D, aquaMat))
Shapes.append(Plane(plane3Normal, plane3D, purpleMat))
Shapes.append(Sphere(sphereCenter, 75, redMat))
Shapes.append(AABB(boxPtA, boxPtB, pinkMat))
#Shapes.append(CylinderY(cylinderBase, cylinderHeight, cylinderRad, yellowMat))

# Game Loop
while not done:
    # Update (Determine all intersection points (Vector3's), and append them
    #         to a list of 2D tuples that will be used in the draw section)
    intPoints = []
    intColors = []
 #   for i in range(1, len(Shapes)):
 #       result = Shapes[i].rayHit(Shapes[0])
 #       if result:
 #           for p in result.mIntersectionPoints:
 #              intPoints.append(p.iTuple()[0:2])
 #               intColors.append(result.mHitObject.mMaterial.getPygameColor())

    # Input (I'm only using "device-polling" here)
    pygame.event.get()
    keys = pygame.key.get_pressed()
    mbut = pygame.mouse.get_pressed()
    mpos = pygame.mouse.get_pos()
    if keys[pygame.K_ESCAPE]:
        done = True
    if mbut[0]:
        Shapes[0].Origin = mpos
    elif mbut[2]:
        # FINISH ME!!
        # Make the ray point towards the mouse *IF* the mouse
        # is a few pixels away from the ray origin
        x = VectorN(mpos) - VectorN((Shapes[0].Origin[0],Shapes[0].Origin[1]))
        if x.magnitude() > 2:
            newDir = x.normalized_copy()
            Shapes[0].rayDir = newDir

    # Draw
    screen.fill((0,0,0))
    for s in Shapes:
        s.pygameRender()
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
