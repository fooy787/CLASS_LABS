"use strict";

const http = require('http');
const ws = require('faye-websocket');
const url = require('url');
const fs = require('fs');
const zip = require('./zip.js')

function getFrame()
{
	fs.open("v01.zip", "r", (err, fd) => {
		if(err)
		{
			console.log(err);
			return;
		}
		zip.open(fd, (err, z) =>
		{
			if(err)
			{
				console.log(err);
				return;
			}
			let mData = z.getEntries();
			let clients=[];
			let frameCount = 0;
	
			let server = http.createServer();
			
			server.on("error", (err) =>
			{
				console.log(err);
				return;
			});
			
			server.on("request",sendFile);
			
			server.on("upgrade", (req,sock,body) => {
				if( ws.isWebSocket(req) ){
					
					let W = new ws(req,sock,body);
					
					W.on("error", (err) =>
					{
						console.log(err);
						return;
					});
					
					W.on("message",(msg) => {
						if(frameCount >= mData.length)
						{
							frameCount = 0;
						}
						z.extract(mData[frameCount], (err, data) =>
						{
							if(err)
							{
								console.log(err);
								return;
							}
							for(let j=0;j<clients.length;++j){
								clients[j].send(data);
								//console.log("Sent message: "+ frameCount);
							}
							frameCount++;
						});
					});
					W.on("close", () => {
						console.log("Websocket closed!");
						for(let i=0;i<clients.length;++i){
							if( clients[i] === W ){
								clients.splice(i,1);
								i--;
							}
						}
					});
					W.on("open",() => {
						console.log("Websocket open!");
						clients.push(W);
						//FileOpen();
					});
				}
			});
			server.listen(44445,'127.0.0.1');
			console.log("Listening on port 44445");
			
			/*for(let iterator = 0; iterator < mData.length; iterator++)
			{
				);
			}*/ //move me to send a frame on a message
		});
	});
}

function main(){
	getFrame();
}

function sendFile(req,resp){
    let pathname = req.url;
    
    //if the browser asked for the site root (/),
    //send the default document
    if( pathname === "/" )
        pathname = "/client.html";
        
    //strip off the leading slash
    pathname = pathname.substring(1);
    
    console.log("GET",pathname);

    //if the path contains slashes, reject it
    if( pathname.indexOf("/") !== -1 || pathname.indexOf("\\") !== -1 ){
        resp.writeHead(403,"Forbidden");
        resp.end();
    }
    else {
        try{
            let rs = fs.createReadStream( pathname, {encoding: "utf8"} );
            rs.on("error", function(){
                console.log("Could not open",pathname);
                resp.writeHead(404);
                resp.end();
            });
            let type = "application/octet-stream";
            if( pathname.indexOf(".html") !== -1 )
                type = "text/html";
            else if( pathname.indexOf(".js") !== -1 )
                type = "application/javascript";
                
            resp.writeHead(200,"OK",{ "Content-type":type });
            rs.pipe(resp);
        } catch( e ){
            console.log("Uh oh!", e);
            resp.writeHead(404,"Not found");
            resp.end();
        }
    }
}

main();