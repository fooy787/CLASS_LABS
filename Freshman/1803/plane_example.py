import pygame
import math3d

N = math3d.VectorN((0.2, 0.8, 0)).normalized_copy()
d = 500


pygame.display.init()
pygame.font.init()
screen = pygame.display.set_mode((800,600))
font = pygame.font.SysFont("Courier New", 15)
pygame.mouse.set_visible(False)

while True:
    pygame.event.get()
    keys = pygame.key.get_pressed()
    if keys[pygame.K_ESCAPE]:
        break
    mpos = pygame.mouse.get_pos()

    dvalue = N.dot(math3d.VectorN((mpos[0], mpos[1], 0)))

    screen.fill((0,0,0))
    # Draw the mouse (and labels)
    pygame.draw.circle(screen, (255,255,255), mpos, 4)
    temps = font.render(str(mpos) + " : " + str(dvalue), False, (255,255,255))
    screen.blit(temps, (mpos[0] - temps.get_width() / 2, mpos[1] - 19))
    # ...draw the plane
    A = [0, d / N[1]]
    B = [800, (d - 800 * N[0]) / N[1]]
    pygame.draw.line(screen, (255,0,0), A, B, 3)

    pygame.display.flip()

pygame.display.quit()

