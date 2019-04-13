import pygame
import time
import math3d
import random

#### READ ME ###
# The entire game so far is in this single file, so I attempted to break it apart into
# different files. Unfortunately, I have no idea what to do because I had an insane
# anount of errors. So if anyone is better at doing it, go right ahead. I'll put my attempts
# at making the new files in the branch folder.


class player (object):
    def __init__ (self, x, y, vx, vy, health):
        self.pPos = math3d.VectorN((x, y))
        self.vx = vx
        self.vy = vy
        self.pHVelocity = math3d.VectorN((vx, 0))
        self.pVVelocity = math3d.VectorN((0, vy))
        self.pHealth = health      
    
    def update (self):
        global player_position
        global player_health
        global enemy_damage
        ## Moves the player based on keys pressed
        if up_pressed == True:
            if right_pressed == True:
                self.pPos += math3d.VectorN((self.vx, -self.vy)) * dtime * 1.5
            elif left_pressed == True:
                self.pPos -= math3d.VectorN((self.vx, self.vy)) * dtime * 1.5
            else:
                self.pPos -= self.pVVelocity * dtime * 2
        elif down_pressed == True:
            if right_pressed == True:
                self.pPos += math3d.VectorN((self.vx, self.vy)) * dtime * 1.5
            elif left_pressed == True:
                self.pPos += math3d.VectorN((-self.vx, self.vy)) * dtime * 1.5
            else:
                self.pPos += self.pVVelocity * dtime * 2        
        elif left_pressed == True:
            self.pPos -= self.pHVelocity * dtime * 2
        elif right_pressed == True:
            self.pPos += self.pHVelocity * dtime * 2

        # Enemy/player detection. 
        for bad in enemyList:
            a = pygame.Rect(self.pPos[0], self.pPos[1], sprite.get_width(), sprite.get_width())
            b = pygame.Rect(enemy_position[0], enemy_position[1], enemy_sprite.get_width(), enemy_sprite.get_height())
            if a.colliderect(b):
                self.pHealth -= enemy_damage

        # sets global variables to be used in other classes
        player_position = self.pPos
        player_health = self.pHealth


    def render (self, win):
        win.blit(sprite, self.pPos - math3d.VectorN(sprite.get_size()) / 2)    

def healthbar (object):
    def __init__ (self):
        pass
    
    def update (self, health):
        self.health = health

    def render (self):
        pass
        #render stuff here


class enemy (object):
    def __init__ (self, ex, ey, evx, evy, health, dam):
        self.ePos = math3d.VectorN((ex, ey))
        self.eVel = math3d.VectorN((evx, evy))
        self.evx = evx
        self.evy = evy
        self.eHVelocity = math3d.VectorN((evx, 0))
        self.eVVelocity = math3d.VectorN((0, evy))
        self.eHealth = health
        self.eDam = dam
        

    def update (self):
        global enemy_position
        global bullet_damage
        global player_position
        global enemy_damage
        ## Enemy follows the player
        if self.ePos[1] > player_position[1]:
            if self.ePos[0] < player_position[0]:
                self.ePos += math3d.VectorN((self.evx, -self.evy)) * dtime# * 1.5
            elif self.ePos[0] > player_position[0]:
                self.ePos -= math3d.VectorN((self.evx, self.evy)) * dtime# * 1.5
            else:
                self.ePos -= self.eVVelocity * dtime * 2
        
        elif self.ePos[1] < player_position[1]:
            if self.ePos[0] < player_position[0]:
                self.ePos += math3d.VectorN((self.evx, self.evy)) * dtime# * 1.5
            elif self.ePos[0] > player_position[0]:
                self.ePos += math3d.VectorN((-self.evx, self.evy)) * dtime# * 1.5
            else:
                self.ePos += self.eVVelocity * dtime * 2

        #bullet/enemy collision detection. If hit, the enemy's health is lowed by the bullet damage.
        # If health is < 0, the enemy is removed from the enemy list               
        for b in bulletList:
            a = pygame.Rect(self.ePos[0], self.ePos[1], enemy_sprite.get_width(), enemy_sprite.get_height())
            c = pygame.Rect(bullet_position[0], bullet_position[1], bullet_sprite.get_width(), bullet_sprite.get_height())
            if a.colliderect(c):
                self.eHealth -= bullet_damage
                bulletList.remove(b)
        if self.eHealth <= 0:
            enemyList.remove(bad)

        # sets global variables to be used in other classes    
        enemy_position = self.ePos
        enemy_damage = self.eDam
        
        
    def render (self, win):
        win.blit(enemy_sprite, self.ePos - math3d.VectorN(enemy_sprite.get_size()) / 2)
        
        
class bullet (object):
    def __init__ (self, x, y, mposx, mposy, dam):
        self.bPos = math3d.VectorN((x, y))
        self.bVel = (math3d.VectorN((mposx, mposy)) - self.bPos).normalized_copy()
        self.bDam = dam
        
    def update (self):
        global bullet_position
        global bullet_damage
        self.bPos += self.bVel * dtime * 4000
        bullet_position = self.bPos
        bullet_damage = self.bDam
        
        
    def render (self, win):
        win.blit(bullet_sprite, self.bPos - math3d.VectorN(bullet_sprite.get_size()) / 2)
        

####################
#  MAIN GAME LOOP  #
####################


pygame.init()


win_size = (1000, 600)
win = pygame.display.set_mode(win_size)
bgcolor = (0, 0, 0)
start_time = time.time()


up_pressed = False
down_pressed = False
left_pressed = False
right_pressed = False


sprite = pygame.image.load('test_fetus.png').convert_alpha()
enemy_sprite = pygame.image.load('banana.png').convert_alpha()
bullet_sprite = pygame.image.load('ball.jpg').convert_alpha()


player_velocity = 200.0
player_position = (win_size[0] / 2, win_size[1] / 2 )
enemy_position = (0, 0)
bullet_damage = 1

#define classes/class lists
bob = player(player_position[0], player_position[1], player_velocity, player_velocity, 10)
enemyList = []
fireList = []
bulletList = []

# Creates enemies. This part definitely needs work as it spawns them all at once.
# Once the level timer is set, the enemies need to spawn a certain # every x seconds
# Each one of the for loops spawns enemies in a different corner just outside of the window.
# I don't know if it's the right way, but hey, it works.
for badt in range(1):
    badt = enemy(random.randint(-100, win_size[0] + 100), random.randint(-100, 0), 100.0, 100.0, 2, 1)
    enemyList.append(badt)
for badb in range(1):
    badb = enemy(random.randint(-100, win_size[0] + 100), random.randint(win_size[1], win_size[1] + 100), 100.0, 100.0, 2, 1)
    enemyList.append(badb)
for badl in range(2):
    badl = enemy(random.randint(-100, 0), random.randint(win_size[1] - 100, win_size[1] + 100), 100.0, 100.0, 2, 1)
    enemyList.append(badl)
for badr in range(2):
    badr = enemy(random.randint(win_size[0], win_size[0] + 100), random.randint(win_size[1] - 100, win_size[1] + 100), 100.0, 100.0, 2, 1)
    enemyList.append(badr)





done = False
while not done:
    
    
    #time stuff
    stop_time = time.time()
    dtime = stop_time - start_time
    start_time = stop_time
            
    mpos = math3d.VectorN(pygame.mouse.get_pos())

    # Keys pressed/bullets fired
    evt = pygame.event.poll()
    if evt.type == pygame.QUIT:
        done = True
    elif evt.type == pygame.KEYDOWN:
        if evt.key == pygame.K_a:
            left_pressed = True
        elif evt.key == pygame.K_d:
            right_pressed = True
        elif evt.key == pygame.K_w:
            up_pressed = True
        elif evt.key == pygame.K_s:
            down_pressed = True
        elif evt.key == pygame.K_ESCAPE:
            done = True
    elif evt.type == pygame.KEYUP:
        if evt.key == pygame.K_a:
            left_pressed = False
        elif evt.key == pygame.K_d:
            right_pressed = False
        elif evt.key == pygame.K_w:
            up_pressed = False
        elif evt.key == pygame.K_s:
            down_pressed = False
    elif evt.type == pygame.MOUSEBUTTONDOWN:
        if evt.button == 1:
            b = bullet(player_position[0], player_position[1], mpos[0], mpos[1], bullet_damage)
            bulletList.append(b)
         
                
    #update classes
    bob.update()

    
    #render classes
    win.fill(bgcolor)
    bob.render(win)
    
    for b in bulletList:
        b.update()
        b.render(win)
        if bullet_position[0] < 0 or bullet_position[0] > win_size[0]:
            bulletList.remove(b)
        elif bullet_position[1] < 0 or bullet_position[1] > win_size[1]:
            bulletList.remove(b)
        
    for bad in enemyList:
        bad.update()
        bad.render(win)
               

    #direction
    pygame.draw.line(win, (255, 0, 0), player_position, mpos, 2) # for testing
    player_dir = mpos - math3d.VectorN(player_position)
    
    pygame.display.flip()

pygame.quit()


