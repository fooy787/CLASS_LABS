import pygame
import math3d
import math
import random
import physics2d
import boulder

class Laser(physics2d.PBody):
    def __init__(self, pos, rad, maxDist):
        super().__init__(pos, rad, 0)

        self.mRotationSpeed = math.pi  / 10    # Radians / s
        self.setOrientation(random.uniform(-math.pi, math.pi))
        self.mMaxDist = maxDist
        self.mEndPos = self.mOrientation * maxDist

    def update(self, dt, pbodyList):
        super().update(dt)

        # Rotate the laser
        self.setOrientation(self.mRadians + self.mRotationSpeed * dt)

        # Reset the max end-point
        self.mEndPos = self.mPos + self.mMaxDist * self.mOrientation

        # Check for boulder hits.
        smallestDist = None
        hitObject = None
        for b in pbodyList:
            result = physics2d.rayCircleHitTest(self.mPos, self.mOrientation, b.mPos, b.mRadius)
            if result != None and (smallestDist == None or result < smallestDist):
                smallestDist = result
                hitObject = b

        if smallestDist != None:
            self.mEndPos = self.mPos + self.mOrientation * smallestDist
            if isinstance(hitObject, boulder.Boulder):
                F = 7000000 * (hitObject.mPos - self.mEndPos).normalized_copy()
                hitObject.applyForce(F, dt)
            else:
                # The player
                hitObject.applyPain()

    def render(self, surf):
        pygame.draw.circle(surf, (64,0,0), self.mPos.iTuple(), self.mRadius)
        pygame.draw.circle(surf, (255,0,0), self.mPos.iTuple(), self.mRadius, 1)
        pygame.draw.line(surf, (255,0,0), self.mPos.iTuple(), self.mEndPos.iTuple())
        pygame.draw.circle(surf, (255,0,0), self.mEndPos.iTuple(), 5, 1)

