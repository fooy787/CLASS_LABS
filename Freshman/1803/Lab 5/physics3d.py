import math3d
import numbers
import pygame

class PBody(object):
    def __init__(self, initPos, boundingRad, mass=1.0, initVel=None):
        if not isinstance(initPos, math3d.VectorN):
            raise TypeError("The initial position must be a VectorN")
        if not isinstance(boundingRad, numbers.Number) or boundingRad <= 0:
            raise TypeError("The bounding radius must be a positive scalar")
        if not isinstance(mass, numbers.Number) or mass <= 0:
            raise TypeError("The mass must be a positive scalar")
        if initVel == None:
            initVel = math3d.VectorN(initPos.mDim)
        if not isinstance(initVel, math3d.VectorN) or initVel.mDim != initPos.mDim:
            raise TypeError("The initial velocity must be None or a vector of the same dimension as initPos")
        self.mPos = initPos.copy()
        self.mVel = initVel.copy()
        self.mMass = mass
        self.mRadians = 0
        self.mRad = boundingRad

    def applyForce(self, F, dt):
        """ Applies a force F applied to this object over a time-span dt
            (often the same as used for update), thus changing its velocity.  """
        self.mVel += dt * F / self.mMass

    def applyFriction(self, accelMag, dt):
        if self.mVel.magnitude() > 0:
            oldVel = self.mVel.copy()
            F = -self.mVel.normalized_copy() * accelMag
            self.applyForce(F * self.mMass, dt)
            # Note: There's a better way to do this using dot-products
            for i in range(oldVel.mDim):
                if oldVel[i] < 0 and self.mVel[i] >= 0 or \
                   oldVel[i] >= 0 and self.mVel[i] < 0:
                    self.mVel = math3d.VectorN(oldVel.mDim)
                    break

    def update(self, dt):
        """ Applies the current velocity to this object's position """
        self.mPos += self.mVel * dt

    def render(self, surf, color=(255,255,255)):
        pygame.draw.circle(surf, color, self.mPos.iTuple(), self.mRad, 1)

