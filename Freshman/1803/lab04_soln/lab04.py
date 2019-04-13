import curve3d
import math3d
import pygame

pygame.display.init()
pygame.font.init()
screen = pygame.display.set_mode((800,600))
font = pygame.font.SysFont("Courier New", 14)

C = curve3d.Curve((0,255,0), font)

mode = "append"
prompts = {"normal":"LDrag: Move C.P./T.H, 'C': toggle closed/open, 'R': remove C.P./T.H., 'A': append C.P./T.H.", \
           "remove":"Click on C.P. to remove. RClick to cancel", \
           "append":"Click to append C.P./T.H. RClick to cancel"}

selectedIndex = None
done = False

while not done:
    # @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    # @ INPUT HANDLING             @
    # @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    eList = pygame.event.get()
    # ... event-handling
    for e in eList:
        if e.type == pygame.QUIT:
            done = True
        elif e.type == pygame.KEYDOWN:
            if e.key == pygame.K_ESCAPE:
                done = True
            elif mode == "normal" and e.key == pygame.K_c:
                C.toggleClosed()
            elif mode == "normal" and e.key == pygame.K_r:
                mode = "remove"
            elif mode == "normal" and e.key == pygame.K_a:
                mode = "append"
        elif e.type == pygame.MOUSEBUTTONDOWN:
            if e.button == 1:
                if mode == "normal":
                    selectedIndex, isControlPoint = C.findClosestControlPoint(math3d.VectorN(e.pos))
                elif mode == "append":
                    if len(C.mControlPoints) > 0:
                        C.addControlPoint(math3d.VectorN(pygame.mouse.get_pos()), C.mTangentHandles[-1])
                    else:
                        C.addControlPoint(math3d.VectorN(pygame.mouse.get_pos()), math3d.VectorN((300,0)))
                elif mode == "remove":
                    tempIndex, isControlPoint = C.findClosestControlPoint(math3d.VectorN(e.pos))
                    C.removeControlPoint(tempIndex)
            elif e.button == 3:
                mode = "normal"
        elif e.type == pygame.MOUSEBUTTONUP and e.button == 1:
            selectedIndex = None
    # ... device-polling
    if selectedIndex != None:
        C.moveControlPointOrHandle(selectedIndex, isControlPoint, math3d.VectorN(pygame.mouse.get_pos()))

    # @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    # @ DRAWING                    @
    # @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    screen.fill((0,0,0))
    C.render(screen)
    temps = font.render(prompts[mode], False, (128,128,128))
    screen.blit(temps, (5,screen.get_height() - temps.get_height()))
    pygame.display.flip()

pygame.display.quit()
pygame.font.quit()