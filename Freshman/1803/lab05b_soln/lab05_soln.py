import pygame
import laser
import random
import math3d
import boulder
import player
import physics2d


class Game(object):
    def __init__(self, numBoulders):
        self.pygameSetup()
        self.mNumBoulders = numBoulders
        self.startNewGame()


    def pygameSetup(self):
        pygame.display.init()
        pygame.mixer.init()
        pygame.font.init()
        self.mScreen = pygame.display.set_mode((800,600))
        self.mClock = pygame.time.Clock()
        self.mFont = pygame.font.SysFont("Courier New", 14)
        self.mBigFont = pygame.font.SysFont("Times New Roman", 32)


    def pygameShutdown(self):
        pygame.font.quit()
        pygame.mixer.quit()
        pygame.display.quit()


    def startNewGame(self):
        # Game-state variables
        self.mMode = "normal"
        self.mPaused = False

        # Create the boulders
        boulderSpeedRange = 50
        self.mBoulders = []
        while len(self.mBoulders) < self.mNumBoulders:
            r = random.randint(5, 32)
            pos = self.findSuitablePosition(r, self.mScreen.get_rect())
            if pos == None:
                break
            vel = math3d.VectorN((random.uniform(-boulderSpeedRange, boulderSpeedRange), \
                           random.uniform(-boulderSpeedRange, boulderSpeedRange)))
            self.mBoulders.append(boulder.Boulder(pos, vel, r))

        # Create the laser
        laserRad = 10
        pos = self.findSuitablePosition(laserRad, self.mScreen.get_rect())
        self.mLaser = laser.Laser(pos, laserRad, self.mScreen.get_width() + self.mScreen.get_height())

        # Create the player
        radius = 15
        self.mPlayer = player.Player(self.findSuitablePosition(radius, self.mScreen.get_rect()), radius)
        self.mTimeSurvived = 0.0


    def findSuitablePosition(self, rad, boundsRect, maxAttempts = 1000):
        attempts = 0
        while attempts < maxAttempts:
            x = random.randint(boundsRect[0], boundsRect[0] + boundsRect[2])
            y = random.randint(boundsRect[1], boundsRect[1] + boundsRect[3])
            pos = math3d.VectorN((x,y))
            ok = True

            if hasattr(self, "mBoulders"):
                for b in self.mBoulders:
                    if (b.mPos - pos).magnitude() < rad + b.mRadius:
                        ok = False
                        break

            if ok and hasattr(self, "mLaser"):
                if (self.mLaser.mPos - pos).magnitude() < rad + self.mLaser.mRadius:
                    ok = False

            if ok and hasattr(self, "mPlayer"):
                if (pos - self.mPlayer.mPos).magnitude() < rad + self.mPlayer.mRadius:
                    ok = False

            if ok:
                return pos
            attempts += 1
        return None


    def update(self):
        if self.mMode == "normal":
            dt = self.mClock.tick() / 1000.0
            if self.mPaused:
                dt = 0

            self.mTimeSurvived += dt

            # Update the boulders
            for b in self.mBoulders:
                # ... see if it is affected by the player's shout
                F = self.mPlayer.calculateShoutForce(b)
                if F: b.applyForce(F, dt)

                # ... normal updates
                b.update(dt)
                b.wallBounce(self.mScreen.get_rect())
                # ... see if it hit the player
                if (self.mPlayer.mPos - b.mPos).magnitude() < self.mPlayer.mRadius + b.mRadius:
                    self.mPlayer.applyPain()
            hitBoulders = physics2d.inellasticCollision(self.mBoulders + [self.mLaser,])
            boulder.Boulder.updateThud(dt)
            if len(hitBoulders) > 0:
                boulder.Boulder.playThud()

            # Update the laser
            self.mLaser.update(dt, self.mBoulders + [self.mPlayer,])

            # Update the player
            self.mPlayer.update(dt)
            if self.mPlayer.mHP <= 0:
                self.mMode = "gameOver"


    def handleInput(self):
        eList = pygame.event.get()
        for e in eList:
            if e.type == pygame.QUIT:
                self.mMode = "done"
            elif e.type == pygame.KEYDOWN:
                if e.key == pygame.K_ESCAPE:
                    self.mMode = "done"
                if e.key == pygame.K_p and self.mMode == "normal":
                    self.mPaused = not self.mPaused


        if self.mMode == "normal":
            self.mPlayer.pointTowards(pygame.mouse.get_pos())
        elif self.mMode == "gameOver":
            keys = pygame.key.get_pressed()
            if keys[pygame.K_y]:
                self.startNewGame()
            if keys[pygame.K_n]:
                self.mMode = "done"


    def render(self):
        # Erase
        self.mScreen.fill((0,0,0))

        # Draw the player shout cone (if any) at the bottom
        self.mPlayer.renderCone(self.mScreen)

        # Then the boulders
        for b in self.mBoulders:
            b.render(self.mScreen)

        # Then the laser
        self.mLaser.render(self.mScreen)

        # Then the player
        self.mPlayer.render(self.mScreen)

        # Last of all, any on-screen text
        # ... fps counter (for debugging)
        tempS = self.mFont.render("FPS: " + str(round(self.mClock.get_fps(),1)), False, (255,255,255))
        self.mScreen.blit(tempS, (self.mScreen.get_width() - 100, self.mScreen.get_height() - 20))
        # ... time-survived
        tempS = self.mFont.render("Time Survived: " + str(round(self.mTimeSurvived, 1)), False, (255,255,255))
        self.mScreen.blit(tempS, (400,5))
        # ... game-over screen (if the game has ended)
        if self.mMode == "gameOver":
            tempS = self.mBigFont.render("GAME OVER!", False, (255,255,255), (64,64,64))
            self.mScreen.blit(tempS, ((self.mScreen.get_width() - tempS.get_width()) / 2, \
                                      (self.mScreen.get_height() - tempS.get_height()) / 2))
            tempS = self.mFont.render("Continue (Y/N)?", False, (255,255,255), (64,64,64))
            self.mScreen.blit(tempS, ((self.mScreen.get_width() - tempS.get_width()) / 2, \
                                      (self.mScreen.get_height() - tempS.get_height()) / 2 + 30))


        pygame.display.flip()

    def run(self):
        while self.mMode != "done":
            self.update()
            self.handleInput()
            self.render()


if __name__ == "__main__":
    G = Game(35)
    G.run()