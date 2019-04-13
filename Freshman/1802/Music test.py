import pygame
import time

#Music

class PlayMusic(object):
    def __init__(self, song):
        pygame.mixer.init()
        self.song = song
        pygame.mixer.music.load(str(self.song))
        pygame.mixer.music.play(-1)

    def Fadeout(self, FadeTime):
        self.timeToFade = FadeTime
        pygame.mixer.music.fadeout(self.timeToFade)
