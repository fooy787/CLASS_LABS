"use strict";

function main(){
    var firstClickLocation;
    window.addEventListener("load",function(){
        var ifr = document.getElementById("theFrame");
        var doc = ifr.contentDocument;
        var cvs = doc.createElement("canvas");
        var ctx = cvs.getContext("2d");
        var img = doc.createElement("img");
        img.src="map.png";
        
        img.addEventListener("load",function(){
            cvs.width = img.width;
            cvs.height = img.height;
            ctx.drawImage(img,0,0);
            ifr.contentWindow.scrollTo(img.width/2,img.height/2);
        });
        
        cvs.addEventListener("click",function(ev){
            var R = cvs.getBoundingClientRect();
            var x = ev.clientX - R.left;
            var y = ev.clientY - R.top;
            console.log(x,y);
            if( firstClickLocation === undefined ){
                firstClickLocation = [x,y];
                ctx.drawImage(img,0,0);
                ctx.fillStyle="yellow";
                ctx.beginPath();
                ctx.arc(x,y,3,0,2.0*3.14159);
                ctx.closePath();
                ctx.fill();
            }
            else{
                var tmp = [x,y];
                console.log("Need path from",firstClickLocation,"to",tmp);
                var xhr = new XMLHttpRequest();
                xhr.responseType="text";
                xhr.addEventListener("load",function(){
                    var path = xhr.responseText;
                    path = path.trim();
                    var L = path.split(" ");
                    ctx.drawImage(img,0,0);
                    
                    ctx.fillStyle="yellow";
                    ctx.beginPath();
                    ctx.arc(parseInt(L[0]),parseInt(L[1]),3,0,2.0*3.14159);
                    ctx.closePath();
                    ctx.fill();
                    ctx.beginPath();
                    ctx.arc(parseInt(L[L.length-2]),parseInt(L[L.length-1]),3,0,2.0*3.14159);
                    ctx.closePath();
                    ctx.fill();
                    
                    ctx.strokeStyle="red";
                    ctx.beginPath();
                    ctx.moveTo(parseInt(L[0]),parseInt(L[1]));
                    for(var i=2;i<L.length;i+=2){
                        ctx.lineTo(parseInt(L[i]),parseInt(L[i+1]));
                    }
                    ctx.stroke();
                });
                xhr.open("GET","/path"+
                    "?firstX="+firstClickLocation[0]+
                    "&firstY="+firstClickLocation[1]+
                    "&secondX="+tmp[0]+
                    "&secondY="+tmp[1]
                );
                xhr.send(null);
                firstClickLocation=undefined;
            }
        });        
        doc.body.appendChild(cvs);
    });
}

main();