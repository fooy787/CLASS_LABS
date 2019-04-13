import random
import urllib.request
import threading
import time


numR = 400
numT = 16
L=threading.Lock()

def work(tid):
    R = random.Random(100+tid)
    pp = numR // 20
    for i in range(numR):
        if i >= pp:
            with L:
                print("Thread",tid,":",i,"of",numR)
            pp += pp
        fx = R.randrange(2048)
        fy = R.randrange(2048)
        sx = R.randrange(2048)
        sy = R.randrange(2048)
        u = urllib.request.urlopen("http://localhost:44444/path?firstX=%d&firstY=%d&secondX=%d&secondY=%d"
            % (fx,fy,sx,sy) )

T=[]
startTime = time.time()
for i in range(numT):
    t = threading.Thread(target=work,args=(i,))
    t.start()
    T.append(t)

for t in T:
    t.join()
    
endTime = time.time()
totalReq = numT*numR
totalTime=endTime-startTime

print(totalReq,"requests in", "%.2f" % totalTime,"sec =",totalReq/totalTime,"req/sec")

