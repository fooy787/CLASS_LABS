import math3d
import numbers
import pygame
import math

class PBody(object):
    def __init__(self, initPos, boundingRad, mass=1.0, initVel=None, terminalSpeed=777.7):
        if not isinstance(initPos, math3d.VectorN) or initPos.mDim != 2:
            raise TypeError("The initial position must be a Vector2")
        if not isinstance(boundingRad, numbers.Number) or boundingRad <= 0:
            raise TypeError("The bounding radius must be a positive scalar")
        if not isinstance(mass, numbers.Number) or mass < 0:
            raise TypeError("The mass must be a positive scalar (or 0 to indicate not-movable")
        if initVel == None:
            initVel = math3d.VectorN(initPos.mDim)
        if not isinstance(initVel, math3d.VectorN) or initVel.mDim != 2:
            raise TypeError("The initial velocity must be a Vector2")
        self.mPos = initPos.copy()
        self.mVel = initVel.copy()
        self.mMass = mass                                 # The mass of the object.  If 0, it is "immovable"
        self.mRadians = 0
        self.mOrientation = math3d.VectorN((1, 0))        # The direction indicated by self.mRadians (make sure to
                                                          #    call setOrientation to modify self.mRadians) or these
                                                          #    will by out of sync.
        self.mRadius = boundingRad
        self.mRadiusSquared = self.mRadius ** 2
        self.mLockOrientationToVelocity = False           # If set to True (externally), the mOrientation and mRadians
                                                          #    values will be auto-updated to match the mVel attribute.
        self.mTerminalSpeed = terminalSpeed
        self.mTerminalSpeedSquared = terminalSpeed ** 2


    def setOrientation(self, newRadians):
        """ Changes the mOrientation and mRadians """
        self.mRadians = newRadians
        self.mOrientation = math3d.VectorN((math.cos(self.mRadians), -math.sin(self.mRadians)))


    def applyAcceleration(self, a, dt):
        """ Applies an acceleration a applied to this object over a time-span dt """
        self.mVel += dt * a


    def applyForce(self, F, dt):
        """ Applies a force F applied to this object over a time-span dt
            (often the same as used for update), thus changing its velocity.  """
        if self.mMass == 0:
            # A "massless" object is used to indicate an immovable object.  Just treat
            # the force as an acceleration
            self.applyAcceleration(F, dt)
        else:
            self.mVel += dt * F / self.mMass


    def applyFriction(self, accelMag, dt):
        if self.mVel.magnitude() > 0:
            oldVel = self.mVel.copy()
            F = -self.mVel.normalized_copy() * accelMag
            self.applyForce(F, dt)
            # Note: This is the better way to do it (rather than the lab3 way)
            if oldVel.dot(self.mVel) < 0:
                self.mVel = math3d.VectorN(oldVel.mDim)


    def wallBounce(self, boundsRect):
        """ (2D ONLY) Makes this object bounce off the internal edges of the given rectangle.
            Returns True if there was a hit. """
        result = False
        if self.mPos[0] < self.mRadius + boundsRect[0]:
            self.mPos[0] = self.mRadius + boundsRect[0]
            self.mVel[0] = -self.mVel[0]
            result = True
        if self.mPos[0] > boundsRect[0] + boundsRect[2] - self.mRadius:
            self.mPos[0] = boundsRect[0] + boundsRect[2] - self.mRadius
            self.mVel[0] = -self.mVel[0]
            result = True
        if self.mPos[1] < self.mRadius + boundsRect[1]:
            self.mPos[1] = self.mRadius + boundsRect[1]
            self.mVel[1] = -self.mVel[1]
            result = True
        if self.mPos[1] > boundsRect[1] + boundsRect[3] - self.mRadius:
            self.mPos[1] = boundsRect[1] + boundsRect[3] - self.mRadius
            self.mVel[1] = -self.mVel[1]
            result = True
        return result


    def update(self, dt):
        """ Applies the current velocity to this object's position """
        # Make sure we're under the terminal speed
        if self.mVel.dot(self.mVel) > self.mTerminalSpeedSquared:
            self.mVel = self.mVel.normalized_copy() * self.mTerminalSpeed

        # Actually move the object
        self.mPos += self.mVel * dt

        # Update the facing direction (if it's "locked" to velocity)
        if self.mLockOrientationToVelocity and self.mVel.magnitudeSquared() > 0:
            r = math.atan2(-self.mVel[1], self.mVel[0])
            self.setOrientation(r)


    def render(self, surf, color=(255,255,255)):
        """ (Usually for testing) draws a circle at mPos (with radius mRad) """
        pygame.draw.circle(surf, color, self.mPos.iTuple(), self.mRadius, 1)


def inellasticCollision(pbodyList):
    # Reference: http://www.imada.sdu.dk/~rolf/Edu/DM815/E10/2dcollisions.pdf
    collisionList = []
    for i in range(len(pbodyList)):
        for j in range(i + 1, len(pbodyList)):
            a = pbodyList[i]
            b = pbodyList[j]

            dir = b.mPos - a.mPos
            dist = dir.magnitude()
            if dist < a.mRadius + b.mRadius:
                dir = dir / dist

                if a not in collisionList:
                    collisionList.append(a)
                if b not in collisionList:
                    collisionList.append(b)

                # Remove the overlap
                half_overlap = (a.mRadius + b.mRadius - dist) / 2 + 4
                if a.mMass == 0 and b.mMass > 0:
                    # a is immovable, move b by the full amount
                    b.mPos += dir * half_overlap * 2
                elif a.mMass > 0 and b.mMass == 0:
                    # b is immovable, move a by the full amount
                    a.mPos -= dir * half_overlap * 2
                elif a.mMass > 0 and b.mMass > 0:
                    # a and b are both moveable, move them both
                    a.mPos -= dir * half_overlap
                    b.mPos += dir * half_overlap

                # Adjust the velocities (inelastic collision)
                aPreNormal = a.mVel.dot(dir) * dir
                bPreNormal = b.mVel.dot(dir) * dir
                aPreTangent = a.mVel - aPreNormal
                bPreTangent = b.mVel - bPreNormal
                aPreSpeed = aPreNormal.magnitude()
                if aPreNormal.dot(dir) < 0:
                    aPreSpeed = -aPreSpeed
                bPreSpeed = bPreNormal.magnitude()
                if bPreNormal.dot(dir) < 0:
                    bPreSpeed = -bPreSpeed

                aPostTangent = aPreTangent
                bPostTangent = bPreTangent

                if a.mMass > 0 and b.mMass > 0:
                    aPostNormal = dir * (aPreSpeed*(a.mMass - b.mMass) + 2*b.mMass*bPreSpeed) / (a.mMass + b.mMass)
                    bPostNormal = dir * (bPreSpeed*(b.mMass - a.mMass) + 2*a.mMass*aPreSpeed) / (a.mMass + b.mMass)
                    a.mVel = aPostTangent + aPostNormal
                    b.mVel = bPostTangent + bPostNormal
                elif a.mMass > 0:
                    a.mVel = aPreTangent - aPreNormal
                elif b.mMass > 0:
                    b.mVel = bPreTangent - bPreNormal

    return collisionList


def rayCircleHitTest(O, D, C, rad):
    """ Does a ray-circle hit-test.  O = Ray origin, D = Ray direction, C = circle center, r = circle radius.
        Returns None if no hit, the distance along the ray if there is one. """
    dir = C - O
    radSquared = rad ** 2
    dirSizeSq = dir.dot(dir)
    paraSize = dir.dot(D)
    paraSizeSq = paraSize ** 2
    perpSizeSq = dirSizeSq - paraSizeSq
    if perpSizeSq < radSquared:
        # Hit!
        f = (radSquared - perpSizeSq) ** 0.5
        t = paraSize - f
        if t > 0:
            return t
    return None


