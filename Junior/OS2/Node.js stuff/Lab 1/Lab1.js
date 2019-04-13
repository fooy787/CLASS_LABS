"use strict";

const fs = require("fs");
const fx = require("fs");

function main(){
    //argv[0] = 'node'
    //argv[1] = name of this file
    let fname = process.argv[2];
	let outname = process.argv[3];
    let buff = Buffer.alloc(512);
	let buffOut = Buffer.alloc(512);
	let Pos = 0;
        
    fs.open(fname, "r", (err,fd) => 
	{
		if( err )
		{
            console.log("Oops.",err);
            return;
        }
		
		fs.open(outname, "w+", (err, fq) => 
		{
			function reader(err,count,buff)
			{
				if( err )
				{
					console.log(err);
					return;
				}
				if( count === 0 )
					return;
				let s = buff.toString("utf8",0,count);
				//console.log(s);
				//buffOut.write(s);
				//Pos++;
				fs.read(fd,buff,0,buff.length,null,writer);
			}
			function writer(err, count, buff)
			{
				if(err)
				{
					console.log(err);
					return;
				}
				if(count === 0)
					return;
				
				fs.write(fq, buff, 0, buff.length, null, reader);
			}
			if( err )
			{
				console.log("Oops.",err);
				return;
			}
			fs.read(fd, buff, 0, buff.length, null, writer);
		});
        //file descriptor, buffer, offset in buffer,
        //count, file position		
    });
}
main();