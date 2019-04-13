import pygame
import math3d
import math
import objects3d

class Raytracer(object):
    def __init__(self, renderSurface, renderType):
        # Save a reference to the render surface
        self.mSurf = renderSurface
        self.mViewAspect = self.mSurf.get_width() / self.mSurf.get_height()
        self.mRenderType = renderType
		
        # The renderable objects
        self.mObjects = []

        # Camera-related attributes (default values)
        self.setCamera(math3d.VectorN((0,0,100)), math3d.VectorN((0,0,0)), \
                       math3d.VectorN((0,1,0)), 70.0, 1.0)



    def setCamera(self, camPos, camCOI, camUp, camFOV, camNear, debug=False):
        """ Adjusts the camera position and other related attributes.
            camPos: the 3d position of the camera
            camCOI: the 3d position of what the camera's pointing at
            camUp: the *general* up direction of the camera's world
            camFOV: the number of degrees indicating the camera's fov
            camNear: the distance (from the camera) of the virtual pixel plane """
        # Create the camera's frame-of-reference (origin and axes) [Left-handed]
        self.mCamPos = camPos
        self.mCamZ = (camCOI - camPos).normalized_copy()
        self.mCamX = math3d.cross(camUp, self.mCamZ).normalized_copy()
        self.mCamY = math3d.cross(self.mCamZ, self.mCamX)

        # Calculate the camera's (relative) horizontal and vertical half-distances
        self.mCamHFov = math.radians(camFOV / 2)
        self.mCamNear = camNear
        self.mViewHHeight = math.tan(self.mCamHFov) * self.mCamNear
        self.mViewHWidth = self.mViewAspect * self.mViewHHeight
        self.mViewPlaneOrigin = self.mCamPos + camNear * self.mCamZ + \
                                             - self.mViewHWidth * self.mCamX + \
                                             self.mViewHHeight * self.mCamY

        # (Debug)
        if debug:
            print("camera position = " + str(self.mCamPos))
            print("camera center-of-interest = " + str(camCOI))
            print("camera (general) up direction = " + str(camUp))
            print("camera field-of-view = " + str(camFOV) + " degrees")
            print("camera near distance = " + str(camNear) + " world units")
            print("----- (setCamera calculations) -----")
            print("camera localX = " + str(self.mCamX))
            print("camera localY = " + str(self.mCamY))
            print("camera localZ = " + str(self.mCamZ))
            print("pygame window dimensions = (" + str(self.mSurf.get_width()) + " x " + str(self.mSurf.get_height()) + ")")
            print("view plane dimensions = (" + str(self.mViewHWidth * 2) + " x " + str(self.mViewHHeight * 2) + ") world units")
            print("view plane (and pygame window) aspect ratio = " + str(self.mViewAspect))
            print("view plane origin = " + str(self.mViewPlaneOrigin))


    def calculatePixelPos(self, ix, iy, printResult=False):
        """ Returns a 3d VectorN indicating where this pygame pixel would
            exist in our 3d virtual world if the pixel plane is centered
            directly in front of the camera (a distance of -near units in front
            of the camera). """
        p = self.mViewPlaneOrigin + \
            self.mCamX * ix * 2 * self.mViewHWidth / (self.mSurf.get_width() - 1) + \
            -self.mCamY * iy * 2 * self.mViewHHeight / (self.mSurf.get_height() - 1)
        if printResult:
            print("pygame [" + str(ix) + ", " + str(iy) + "] = " + str(p) + " on the view plane (in world units)")
        return p


    def rayCast(self, R):
        """ Sends the given ray into our virtual world and returns the intersection
            data (or None if no hit) for the closest object hit. """
        closestHitData = None
        smallestT = None
        for o in self.mObjects:
            result = o.rayHit(R)
            if result != None and len(result.mIntersectionDistances) > 0:
                minT = min(result.mIntersectionDistances)
                if closestHitData == None or minT < smallestT:
                    smallestT = minT
                    closestHitData = result
        return closestHitData


    def getColorOfHit(self, hitData):
        """ Returns a pygame color tuple corresponding to the object hit by a ray (contained within
            the given objects3d.RayHitResult object.  If hitData is None it means the ray hits nothing
            and a background color should be returned. """
        if hitData == None:
            return (32, 32, 32)
        else:
            return hitData.mHitObject.mMaterial.getPygameColor()

    def renderOneLine(self, iy):
        for ix in range(0, self.mSurf.get_width()):
            P = self.calculatePixelPos(ix, iy)
            if self.mRenderType == "orthogonal":
                R = objects3d.Ray(P, self.mCamZ)
            else:
                R = objects3d.Ray(P, P - self.mCamPos)
            hitResult = self.rayCast(R)
            color = self.getColorOfHit(hitResult)
            self.mSurf.set_at((ix, iy), color)


