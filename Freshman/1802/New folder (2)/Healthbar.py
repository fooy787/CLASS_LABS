### Health bars
import pygame

class Healthbar(object):
    def __init__(self, sprite_health, pos):
        self.CurrentHealth = sprite_health
        self.max_health = 100

        self.SpritePos = pos

        self.HeadBuffer = 30

        self.RectX = self.SpritePos[0] - 15
        self.RectK = self.CurrentHealth / self.max_health
        self.RectW = 30 * self.RectK
        self.RectH = 4
        self.RectY = (self.SpritePos[1] - self.HeadBuffer)

        pygame.draw.rect(win, (255,255,255),(self.RectX, self.RectY, self.RectW, self.RectH))


##pygame.init()
##
##win_size = (640, 480)
##win = pygame.display.set_mode(win_size)
##posX = 320
##posY = 240
##SpriteHealth = 100
##
##temp = Healthbar(SpriteHealth, (posX, posY))
##done = False
##while not done:
##    evt_list = pygame.event.get()
##    for evt in evt_list:
##        if evt.type == pygame.QUIT:
##            done = True
##
##    pygame.draw.circle(win, (255, 0, 0),(posX, posY), 20)
##    pygame.display.flip()
##pygame.quit()
