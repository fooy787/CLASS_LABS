import pygame
import time

#Music

class Sound(object):
    def __init__(self, audio):
	"""Pass in the ogg file as in: testSound.ogg   --Very important to have the extention on it"""
        pygame.mixer.init()
        self.audio = audio
        pygame.mixer.music.load(str(self.audio))
	pygame.mixer.music.play()

    def Fadeout(self, FadeTime):
	"""Pass in the fade time, as in how long you want it to take for it to fadeout. Will end the sound at the end of the fadeout time."""
        self.timeToFade = FadeTime
        pygame.mixer.music.fadeout(self.timeToFade)

    def Repeat(self):
    """Call this to get it to infinitly repeat"""
	pygame.mixer.music.play(-1)
		
    def StopSound(self):
    """Stops the music"""
	pygame.mixer.music.stop()
