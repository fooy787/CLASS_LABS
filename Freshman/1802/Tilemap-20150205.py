import pygame
import xml.etree.ElementTree as ET

from mymath import Vector2

class Sprite(object):
    def __init__(self, pos=Vector2(), size=(32, 32)):
        self.pos = pos
        self.size = size
        
        self.prev_pos = Vector2()
        
        self.vel_mag = 640 / 2000.0
    
    def update(self, dtime):
        pressed_keys = pygame.key.get_pressed()
        
        vel = Vector2()
        
        if pressed_keys[pygame.K_LEFT]:
            vel.x = -1
        elif pressed_keys[pygame.K_RIGHT]:
            vel.x = 1
        
        if pressed_keys[pygame.K_UP]:
            vel.y = -1
        elif pressed_keys[pygame.K_DOWN]:
            vel.y = 1
        
        vel *= self.vel_mag
        
        self.prev_pos = self.pos.copy()
        self.pos += vel * dtime
        
        # keep sprite from going out of the window
        self.pos.x = max(0, min(self.pos.x, 640 - self.size[0]))
        self.pos.y = max(0, min(self.pos.y, 480 - self.size[1]))
    
    def render(self, surface):
        x = int(self.pos.x)
        y = int(self.pos.y)
        rect = (x, y, self.size[0], self.size[1])
        pygame.draw.rect(surface, (0, 255, 0), rect)

class Tilemap(object):
    def __init__(self, mapfile):
        self.mapfile = mapfile
        
        tree = ET.parse(mapfile)
        root = tree.getroot()
        
        self.width = int(root.attrib['width'])
        self.height = int(root.attrib['height'])
        self.tilewidth = int(root.attrib['tilewidth'])
        self.tileheight = int(root.attrib['tileheight'])
        
        self.tilelist = [None]  # store tile surface's
        self.tileidx = []       # store tilemap indices
        
        for child in root:
            if child.tag == 'tileset':
                self.parseTileset(child)
            elif child.tag == 'layer':
                self.parseLayer(child)
        
    def parseTileset(self, elem):
        source = elem[0].attrib['source']   # load 'source' from 'image' tag
        img = pygame.image.load(source).convert()
        
        # split tileset into individual tiles
        for y in range(0, img.get_height(), self.tileheight):
            for x in range(0, img.get_width(), self.tilewidth):
                tmpimg = img.subsurface((x, y, self.tilewidth, self.tileheight))
                self.tilelist.append(tmpimg)
    
    def parseLayer(self, elem):
        
        # load tilemap index information from 'layer' tag
        tilecnt = 0
        for child in elem:
            if child.tag == 'data':
                for data in child:
                    if data.tag == 'tile':
                        if tilecnt == 0:
                            self.tileidx.append([])
                        
                        gid = int(data.attrib['gid'])
                        self.tileidx[-1].append(gid)
                        
                        # reset count at layer width (in tiles)
                        tilecnt += 1
                        if tilecnt == self.width:
                            tilecnt = 0
    
    def getColRow(self, pos):
        return (int(pos.x / self.tilewidth), int(pos.y / self.tileheight))
    
    def handleCollision(self, sprite):
        
        # use sprite's current x and previous y to check if there was a
        # collision due to the sprite moving along the x-axis
        tmppos = Vector2(sprite.pos.x, sprite.prev_pos.y)
        start_col, start_row = self.getColRow(tmppos)
        
        # calculate position of sprite's bottom-right corner
        # subtract 1 from sprite's width and height to fix collision issue
        #   where sprite would collide with adjacent, but not overlapping, tiles
        x = tmppos.x + sprite.size[0] - 1
        y = tmppos.y + sprite.size[1] - 1
        end_col, end_row = self.getColRow(Vector2(x, y))
        
        solid = False
        hazard = False
        
        for row in range(start_row, end_row + 1):
            for col in range(start_col, end_col + 1):
                gid = self.tileidx[row][col]
                if gid not in (0, 1, 2, 3, 12):
                    solid = True
                elif gid == 12:
                    hazard = True
        
        if solid:
            if sprite.pos.x > sprite.prev_pos.x:
                sprite.pos.x = int(sprite.pos.x) - (int(sprite.pos.x) % 32)
            elif sprite.pos.x < sprite.prev_pos.x:
                sprite.pos.x = int(sprite.pos.x) + (32 - (int(sprite.pos.x) % 32))
            solid = False
        
        # now use sprite's previous x and current y to check if there was a
        # collision due to the sprite moving along the y-axis
        tmppos.x = sprite.prev_pos.x
        tmppos.y = sprite.pos.y
        start_col, start_row = self.getColRow(tmppos)
        
        x = tmppos.x + sprite.size[0] - 1
        y = tmppos.y + sprite.size[1] - 1
        end_col, end_row = self.getColRow(Vector2(x, y))
        
        for row in range(start_row, end_row + 1):
            for col in range(start_col, end_col + 1):
                gid = self.tileidx[row][col]
                if gid not in (0, 1, 2, 3, 12):
                    solid = True
                elif gid == 12:
                    hazard = True
        
        if solid:
            if sprite.pos.y > sprite.prev_pos.y:
                sprite.pos.y = int(sprite.pos.y) - (int(sprite.pos.y) & 31)
            elif sprite.pos.y < sprite.prev_pos.y:
                sprite.pos.y = int(sprite.pos.y) + (32 - (int(sprite.pos.y) & 31))
        
        if hazard:
            print("I'M DROWNING!!!")
        
    def render(self, surface):
        y = 0
        for row in self.tileidx:
            x = 0
            for col in row:
                tmpimg = self.tilelist[col]
                if tmpimg != None:
                    surface.blit(tmpimg, (x, y))
                
                x += self.tilewidth
        
            y += self.tileheight
                

pygame.init()

window = pygame.display.set_mode((640, 480))
clock = pygame.time.Clock()

tmap = Tilemap('mymap.tmx')
player = Sprite(Vector2(32, 32), (32, 32))

done = False
while not done:
    
    dtime = clock.tick(60)
    
    evtList = pygame.event.get()
    for evt in evtList:
        if evt.type == pygame.QUIT:
            done = True
        elif evt.type == pygame.MOUSEBUTTONDOWN:
            mx, my = evt.pos
            col, row = tmap.getColRow(Vector2(mx, my))
            print("GID:", tmap.tileidx[row][col])
    
    player.update(dtime)
    
    tmap.handleCollision(player)
    
    window.fill((0, 0, 0))
    
    tmap.render(window)
    player.render(window)
    
    pygame.display.flip()

pygame.quit()
