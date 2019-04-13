import math3d
import pygame

drawThickness = 3

class Material(object):
    def __init__(self, diffuseColor, ambColor, SpecularColor, ShininessVal):
        self.mDiffuse = diffuseColor
        self.AmbientColor = ambColor
        self.SpecularColor = SpecularColor
        self.ShininessValue = ShininessVal

    def getPygameColor(self):
        return (255 * self.mDiffuse).iTuple()

class RayHitResult(object):
    def __init__(self, ray, obj):
        self.mRay = ray                   # The ray involved in the collision
        self.mHitObject = obj             # The other object in the collision
        self.mIntersectionPoints = []     # The intersection points on the primitive and ray
        self.mIntersectionDistances = []  # The distances along the ray to each of the intersection points.

    def appendIntersection(self, dist):
        P = self.mRay.getPoint(dist)
        self.mIntersectionPoints.append(P)
        self.mIntersectionDistances.append(dist)


class Ray(object):
    def __init__(self, origin, direction):
        self.mOrigin = origin.copy()
        self.mDirection = direction.normalized_copy()

    def pygameRender(self, surf, name=None, font=None):
        maxDist = surf.get_width() + surf.get_height()
        ptA = self.mOrigin.iTuple()[0:2]
        ptB = self.getPoint(maxDist).iTuple()[0:2]
        pygame.draw.line(surf, (255,255,255), ptA, ptB)
        pygame.draw.circle(surf, (255,255,255), ptA, 5)

        ptC = self.getPoint(20)

        if name != None and font != None:
            temps = font.render(name, False, (255,255,255))
            surf.blit(temps, (ptC[0] - temps.get_width() / 2, \
                              ptC[1] - temps.get_height() / 2))

    def getPoint(self, dist):
        return self.mOrigin + dist * self.mDirection

class Sphere(object):
    def __init__(self, center, radius, material):
        self.mCenter = center.copy()
        self.mRadius = radius
        self.mRadiusSq = radius ** 2
        self.mMaterial = material

    def pygameRender(self, surf, name=None, font=None):
        global drawThickness
        color = self.mMaterial.getPygameColor()
        pygame.draw.circle(surf, color, self.mCenter.iTuple()[0:2], self.mRadius, drawThickness)
        if name != None and font != None:
            temps = font.render(name, False, color)
            surf.blit(temps, (self.mCenter[0] - temps.get_width() / 2, \
                              self.mCenter[1] - temps.get_height() / 2))

    def rayHit(self, R):
        toCenter = self.mCenter - R.mOrigin     # Vector from ray origin to sphere center
        projDist = toCenter.dot(R.mDirection)   # [scalar] Distance along ray to get closest to sphere center
        closestDistSq = toCenter.dot(toCenter) - projDist * projDist
        if closestDistSq >= self.mRadiusSq:
            # No hit!
            return None
        f = (self.mRadiusSq - closestDistSq) ** 0.5

        result = RayHitResult(R, self)
        if toCenter.magnitudeSquared() > self.mRadiusSq:
            # The Ray originates outside of the sphere
            if projDist - f > 0:
                result.appendIntersection(projDist - f)
            if projDist + f > 0:
                result.appendIntersection(projDist + f)
        else:
            # The Ray originates inside the sphere.
            result.appendIntersection(projDist + f)

        return result


class Plane(object):
    def __init__(self, normal, dvalue, material):
        self.mNormal = normal.normalized_copy()
        self.mD = dvalue
        self.mMaterial = material

    def pygameRender(self, surf, name=None, font=None):
        global drawThickness
        if abs(self.mNormal[0]) > abs(self.mNormal[1]):
            # More of a Vertical plane
            ptA = (int(self.mD / self.mNormal[0]), 0)
            y = surf.get_height() - 1
            ptB = (int((self.mD - self.mNormal[1] * y) / self.mNormal[0]), y)
        else:
            # More of a Horizontal plane
            ptA = (0, int(self.mD / self.mNormal[1]))
            x = surf.get_width() - 1
            ptB = (x, int((self.mD - self.mNormal[0] * x) / self.mNormal[1]))
        color = self.mMaterial.getPygameColor()
        pygame.draw.line(surf, color, ptA, ptB, drawThickness)

        if name != None and font != None:
            temps = font.render(name, False, color)
            surf.blit(temps, ((ptA[0] + ptB[0]) / 2 - temps.get_width() / 2, \
                              ( ptA[1] + ptB[1]) / 2 - temps.get_height() / 2 - 10))

    def rayHit(self, R):
        den = R.mDirection.dot(self.mNormal)
        if den == 0.0:
            # Ray is parallel to plane.  No hit.
            return None
        num = self.mD - R.mOrigin.dot(self.mNormal)
        t = num / den
        if t < 0:
            # A "backwards" hit -- ignore
            return None
        result = RayHitResult(R, self)
        result.appendIntersection(t)
        return result


class AABB(object):
    def __init__(self, ptA, ptB, material):
        self.mMinPt = ptA.copy()
        self.mMaxPt = ptB.copy()
        for i in range(ptA.mDim):
            if ptB[i] < self.mMinPt[i]:
                self.mMinPt[i] = ptB[i]
            if ptA[i] > self.mMaxPt[i]:
                self.mMaxPt[i] = ptA[i]
        self.mMaterial = material
        normals = (math3d.VectorN((-1,0,0)), math3d.VectorN((1,0,0)), \
                   math3d.VectorN((0,-1,0)), math3d.VectorN((0,1,0)), \
                   math3d.VectorN((0,0,-1)), math3d.VectorN((0,0,1)))
        self.mPlanes = []
        for i in range(6):
            if i % 2 == 0:
                p = self.mMinPt
            else:
                p = self.mMaxPt
            self.mPlanes.append(Plane(normals[i], normals[i].dot(p), material))

    def pygameRender(self, surf, name=None, font=None):
        global drawThickness
        dim = self.mMaxPt - self.mMinPt
        color = self.mMaterial.getPygameColor()
        pygame.draw.rect(surf, color, self.mMinPt.iTuple()[0:2] + dim.iTuple()[0:2], drawThickness)

        if name != None and font != None:
            temps = font.render(name, False, color)
            surf.blit(temps, (self.mMinPt[0] + dim[0] / 2 - temps.get_width() / 2, \
                              self.mMinPt[1] + dim[1] / 2 - temps.get_height() / 2))

    def rayHit(self, R):
        hitDistances = []
        for i in range(6):
            planeResult = self.mPlanes[i].rayHit(R)
            if planeResult:
                # We hit that bounding plane.  Now see if we hit within the boundaries
                # of the plane.  Hint: if we hit a PLANE, we have exactly one hit (and
                # so one thing in result.mIntersectionDistances
                currentDimension = i // 2
                inBounds = True
                hitPoint = planeResult.mIntersectionPoints[0]
                hitDist = planeResult.mIntersectionDistances[0]
                for j in range(3):
                    if j == currentDimension:
                        continue
                    if hitPoint[j] < self.mMinPt[j] or hitPoint[j] > self.mMaxPt[j]:
                        inBounds = False
                        break
                if inBounds:
                    hitDistances.append(hitDist)
        # If hitDistances is empty, no hit.
        if len(hitDistances) == 0:
            result = None
        else:
            result = RayHitResult(R, self)
            for t in hitDistances:
                result.appendIntersection(t)
        return result


class CylinderY(object):
    def __init__(self, basePos, height, radius, material):
        self.mBase = basePos
        self.mHeight = height
        self.mRadius = radius
        self.mRadiusSq = radius ** 2
        self.mMaterial = material

    def pygameRender(self, surf, name=None, font=None):
        color = self.mMaterial.getPygameColor()
        pygame.draw.rect(surf, color, (self.mBase[0] - self.mRadius, self.mBase[1], self.mRadius * 2, self.mHeight), drawThickness)

        if name != None and font != None:
            temps = font.render(name, False, color)
            surf.blit(temps, (self.mBase[0] - temps.get_width()/2, self.mBase[1] + self.mHeight / 2 - temps.get_height() / 2))

    def rayHit(self, R):
        Ox = R.mOrigin[0]
        Oz = R.mOrigin[2]
        Dx = R.mDirection[0]
        Dz = R.mDirection[2]
        Bx = self.mBase[0]
        Bz = self.mBase[2]
        epsilon = 0.0001

        # Check the sides of the cylinder
        a = Dx ** 2 + Dz ** 2
        b = 2 * (-Bx * Dx - Bz * Dz + Ox * Dx + Oz * Dz)
        c = -2 * Bx * Ox - 2 * Bz * Oz + Bx ** 2 + Bz ** 2 + Ox ** 2 + Oz ** 2 - self.mRadiusSq
        inner = b ** 2 - 4 * a * c
        den = 2 * a
        if inner < 0 or den < epsilon:
            return None

        inner **= 0.5

        root1 = (-b + inner) / den
        root2 = (-b - inner) / den
        result = RayHitResult(R, self)
        if root1 > 0:
            P1 = R.getPoint(root1)
            if P1[1] >= self.mBase[1] - epsilon and P1[1] <= self.mBase[1] + self.mHeight + epsilon:
                result.appendIntersection(root1)
        if root2 > 0:
            P2 = R.getPoint(root2)
            if P2[1] >= self.mBase[1] - epsilon and P2[1] <= self.mBase[1] + self.mHeight + epsilon:
                result.appendIntersection(root2)

        # Now check the top / bottom of the cylinder
        planeT = Plane(math3d.VectorN((0,1,0)), self.mBase[1] + self.mHeight, self.mMaterial)
        planeB = Plane(math3d.VectorN((0,-1,0)), -self.mBase[1], self.mMaterial)
        resultT = planeT.rayHit(R)
        if resultT:
            P = resultT.mIntersectionPoints[0]
            if (P[0] - Bx) ** 2 + (P[2] - Bz) ** 2 < self.mRadiusSq:
                result.appendIntersection(resultT.mIntersectionDistances[0])
        resultB = planeB.rayHit(R)
        if resultB:
            P = resultB.mIntersectionPoints[0]
            if (P[0] - Bx) ** 2 + (P[2] - Bz) ** 2 < self.mRadiusSq:
                result.appendIntersection(resultB.mIntersectionDistances[0])

        if len(result.mIntersectionDistances) > 0:
            return result
        else:
            return None
