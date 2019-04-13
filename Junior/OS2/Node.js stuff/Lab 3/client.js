"use strict";

function main(){
	function sendRequest()
	{
		sock.send("test")
		setTimeout(sendRequest, 50);
	}
    console.log("Establishing connection...");
    let sock = new WebSocket("ws://127.0.0.1:44445/");
	let q = document.getElementById("vid");
	
    
    sock.addEventListener("error", (err) => {
        console.log(err);
    });
    
    sock.addEventListener("message",(e) => {
        let blob = URL.createObjectURL(e.data);
		q.src = blob;
    });
	sock.addEventListener("open", () => {
		console.log("Socket open");
		sendRequest();
	});
	
}

main();

