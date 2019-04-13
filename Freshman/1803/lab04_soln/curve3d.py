import math3d
import pygame


class Curve(object):
    def __init__(self, color, fontObj, segmentResolution=20):
        self.mControlPoints = []
        self.mTangentHandles = []
        self.mDrawPoints = []
        self.mColor = color
        self.mSegmentResolution = segmentResolution
        self.mTangentScale = 0.2
        self.mClosed = False
        self.mFont = fontObj

    def addControlPoint(self, p, t):
        if not isinstance(p, math3d.VectorN) or p.mDim > 3 or p.mDim < 2:
            raise TypeError("p must be a Vector2 or Vector3 object")
        if not isinstance(t, math3d.VectorN) or t.mDim != p.mDim:
            raise TypeError("t must be a VectorN of the same dimension as p")
        self.mControlPoints.append(p)
        self.mTangentHandles.append(t * self.mTangentScale + p)
        self.generateDrawPoints()

    def removeControlPoint(self, index):
        if index != None and index >= 0 and index < len(self.mControlPoints):
            del self.mControlPoints[index]
            del self.mTangentHandles[index]
            self.generateDrawPoints()

    def moveControlPointOrHandle(self, index, isControlPoint, newPos):
        if index >= 0 and index < len(self.mControlPoints):
            if isControlPoint:
                offset = self.mTangentHandles[index] - self.mControlPoints[index]
                self.mTangentHandles[index] = newPos + offset
                self.mControlPoints[index] = newPos.copy()
            else:
                self.mTangentHandles[index] = newPos.copy()
            self.generateDrawPoints()

    def toggleClosed(self):
        self.mClosed = not self.mClosed
        self.generateDrawPoints()

    def findClosestControlPoint(self, p, threshold=10):
        """ Finds either a control point or a "tangent handle" that
            is closest to p (and within the threshold).  If found, a
            reference is returned. """
        closestIndex = None
        closestDist = None
        isControlPoint = None
        for i in range(len(self.mControlPoints)):
            dist = (self.mControlPoints[i] - p).magnitude()
            if dist < threshold and (closestIndex == None or dist < closestDist):
                closestIndex = i
                closestDist = dist
                isControlPoint = True
        for i in range(len(self.mTangentHandles)):
            dist = (self.mTangentHandles[i] - p).magnitude()
            if dist < threshold and (closestIndex == None or dist < closestDist):
                closestIndex = i
                closestDist = dist
                isControlPoint = False
        return closestIndex, isControlPoint


    def generateDrawPoints(self):
        if self.mClosed:
            n = len(self.mControlPoints)
        else:
            n = len(self.mControlPoints) - 1
        inc = 1.0 / (self.mSegmentResolution - 1)
        self.mDrawPoints = []
        for i in range(n):
            u = 0.0
            next = (i + 1) % len(self.mControlPoints)
            p0 = self.mControlPoints[i]
            p1 = self.mControlPoints[next]
            t0 = (self.mTangentHandles[i] - self.mControlPoints[i]) / self.mTangentScale
            t1 = (self.mTangentHandles[next] - self.mControlPoints[next]) / self.mTangentScale
            for j in range(self.mSegmentResolution):
                # Reference: http://en.wikipedia.org/wiki/Cubic_Hermite_spline
                u2 = u * u
                u3 = u2 * u
                c = [2*u3 - 3*u2 + 1, u3 - 2 * u2 + u, -2*u3 + 3*u2, u3 - u2]
                p = c[0] * p0 + c[1] * t0 + c[2] * p1 + c[3] * t1
                self.mDrawPoints.append(p)
                u += inc

    def render(self, surf):
        for i in range(len(self.mDrawPoints) - 1):
            p1 = self.mDrawPoints[i].iTuple()
            if i > 0:
                pygame.draw.circle(surf, (0,255,255), p1, 3)
            p2 = self.mDrawPoints[i + 1].iTuple()
            pygame.draw.line(surf, (255,255,255), p1, p2)
        for i in range(len(self.mControlPoints)):
            p = self.mControlPoints[i]
            tend = self.mTangentHandles[i]
            pygame.draw.line(surf, (64,64,64), p.iTuple(), tend.iTuple())
            pygame.draw.circle(surf, (0,255,0), p.iTuple(), 6)
            pygame.draw.circle(surf, (255,0,0), tend.iTuple(), 5, 1)
            temps = self.mFont.render(str(i), False, (200,200,200))
            surf.blit(temps, (p[0] - temps.get_width()/2, p[1] - temps.get_height() - 4))


