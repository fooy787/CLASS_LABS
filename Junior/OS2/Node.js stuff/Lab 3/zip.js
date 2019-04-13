"use strict";

//jh sp 2018

const fs = require("fs");
const zlib = require("zlib");



const sizeof_ZipLocalFileHeader = 30;
const sizeof_ZipCentralDirectoryHeader = 46;
const sizeof_ZipDataDescriptor = 12;


function parseBuffer(buff,spec){
    let offset=0;
    let H={};
    for(let i=0;i<spec.length;i+=2){
        let type = spec[i];
        let name = spec[i+1];
        if( type === "uint32_t" ){
            H[name] = buff.readUInt32LE(offset);
            offset += 4;
        }
        else if( type === "uint16_t" ){
            H[name] = buff.readUInt16LE(offset);
            offset += 2;
        }
        else{
            throw new Error("Internal error");
        }
    }
    return H;
}

function parse_ZipLocalFileHeader(buff){
    return parseBuffer( buff,
        [
        "uint32_t","sig",  
        "uint16_t","version",
        "uint16_t","flags",
        "uint16_t","method",
        "uint16_t","time",
        "uint16_t","date",
        "uint32_t","crc",
        "uint32_t","compressedSize",
        "uint32_t","uncompressedSize",
        "uint16_t","filenameLength",
        "uint16_t","extraLength"
        ]);
}

function parse_ZipCentralDirectoryHeader(buff){
    return parseBuffer( buff, [
        "uint32_t","sig",
        "uint16_t","cVersion", 
        "uint16_t","eVersion", 
        "uint16_t","flags", 
        "uint16_t","method", 
        "uint16_t","time", 
        "uint16_t","date",
        "uint32_t","crc", 
        "uint32_t","compressedSize", 
        "uint32_t","uncompressedSize",
        "uint16_t","filenameLength", 
        "uint16_t","extraLength", 
        "uint16_t","commentLength", 
        "uint16_t","diskNumber", 
        "uint16_t","iAttributes",
        "uint32_t","eAttributes", 
        "uint32_t","localHeaderOffset"
    ]);
}

function parse_ZipDataDescriptor(buff){
    return parseBuffer( buff, [
        "uint32_t","crc",
        "uint32_t","compressedSize",
        "uint32_t","uncompressedSize"
    ]);
}


function readAll(fp,buff,pos,callback){
    let count=buff.length;
    let offs=0;
    function F(){
        fs.read(fp, buff, offs, count, pos, 
            (err,nr,buff) => {
                if( err ){
                    if( callback )
                        callback(err,buff.length,buff);
                    else
                        throw err;
                }
                pos += nr;
                offs += nr;
                count -= nr;
                if( count > 0 )
                    F();
                else{
                    if(callback)
                        callback(undefined,buff.length,buff);
                }
            }
        );
    }
    F();
} 

class ZipEntry{
    constructor(name, soffs){
        this.name=name;
        this.startingOffset = soffs;
    }
}
            
class Zip{
    /** Initialize the zip reader.
    @param fp A file descriptor, opened for reading in binary mode
    @param callback A function that will be called when the zip file is ready to be used.
            The callback will have the parameters (error,Zip)
    */
    constructor(fp, callback){
        let recordCount=0;
        let recordStart = 0;
        let hdrB = Buffer.alloc(sizeof_ZipLocalFileHeader);
        
        let readLocalFileHeader = () => {
            readAll(fp,hdrB,recordStart, (err) => {
                if(err){
                    if(callback)
                        callback(err,this)
                    else
                        throw err;
                }
                let hdr = parse_ZipLocalFileHeader(hdrB);
                if( hdr.sig == 0x4034b50 ){
                    ++recordCount;
                    recordStart = (recordStart + 
                        sizeof_ZipLocalFileHeader+
                        hdr.filenameLength + 
                        hdr.compressedSize + 
                        hdr.extraLength +
                        //if bit 3 of flags = 1: There's a data descriptor here too
                        ( (hdr.flags & (1<<3)) ? sizeof_ZipDataDescriptor:0 ) );
                    readLocalFileHeader();
                    return;
                }
                else{
                    if( recordCount == 0 ){
                        if(callback)
                            callback(new Error("This does not appear to be a zip file"),this);
                        else
                            throw new Error("This does not appear to be a zip file");
                    }
            
                    let entries = [];
                    let chdrB = Buffer.alloc(sizeof_ZipCentralDirectoryHeader);
                    let i=0;
                    let doIt = () => {
                        if( i === recordCount ){
                            this.entries = entries;
                            this.fp=fp;
                            if(callback)
                                callback(undefined,this);
                        }
                        else{
                            readAll(fp,chdrB,recordStart,(err) => {
                                if( err ){
                                    if( callback )
                                        callback(err,this)
                                    else
                                        throw err;
                                }
                                let chdr = parse_ZipCentralDirectoryHeader(chdrB);
                                if( chdr.sig != 0x2014b50 )
                                    throw new Error("Sig mismatch: Got "+chdr.sig.toString(16));
                                let filenameV = Buffer.alloc(chdr.filenameLength);
                                readAll(fp,filenameV,
                                        recordStart+sizeof_ZipCentralDirectoryHeader,
                                        (err) => {
                                            if(err){
                                                if(callback)
                                                    callback(err,this)
                                                else
                                                    throw err;
                                            }
                                            let filename = filenameV.toString();
                                            let meta = new ZipEntry(filename, chdr.localHeaderOffset);
                                            entries.push(meta);
                                            recordStart += (
                                                sizeof_ZipCentralDirectoryHeader + 
                                                chdr.filenameLength + 
                                                chdr.extraLength + 
                                                chdr.commentLength
                                            );
                                            i++;
                                            doIt();
                                        }
                                );
                            });
                        }
                    }
                    doIt();
                }
            });
        };
        readLocalFileHeader();
    }
        
    /** Get all entries in the zip file.
    @return a list of ZipEntry's*/
    getEntries(){
        return this.entries;
    }
    
    getEntry(name){
        for( let i=0;i<this.entries.length;++i){
            if( this.entries[i].name === name )
                return this.entries[i];
        }
        throw new Error("No such entry "+name+" in zip file");
    }
    
    /** Extract data from the zip file.
        @param ze A ZipEntry
        @param callback The callback. Will be passed (error,Buffer)
        */
    extract(ze, callback){
        if( !ze || ze.startingOffset === undefined )
            if(callback ){
                callback(new Error("Bad ZipEntry"));
                return;
            } else
                throw new Error("Bad ZipEntry");
                
        let recordStart = ze.startingOffset;
        let fp = this.fp;
        let hdrB = Buffer.alloc(sizeof_ZipLocalFileHeader);
        readAll(fp,hdrB,recordStart,(err) => {
            if(err){
                if(callback)
                    callback(err,undefined)
                else
                    throw err;
            }
            let hdr = parse_ZipLocalFileHeader(hdrB);
            
            if( hdr.flags & 1 )
                throw new Error("Cannot extract encrypted file");

            let pos = (
                recordStart + 
                hdrB.length + 
                hdr.filenameLength + 
                hdr.extraLength
             );
        
            if( hdr.method == 0 ){
                //stored
                let data = Buffer.alloc(hdr.uncompressedSize);
                readAll( this.fp, data, pos, (err) => {
                    if(err){
                        if(callback)
                            callback(err,undefined)
                        else
                            throw err;
                    }
                    if(callback)
                        callback(undefined,data);
                });
            } else if( hdr.method == 8 ){
                //deflate
                let data = Buffer.alloc(hdr.compressedSize);
                readAll( this.fp, data,pos, (err) => {
                    if(err){
                        if(callback)
                            callback(err,undefined)
                        else
                            throw err;
                    }
                    zlib.inflateRaw(data,{windowBits: 15}, (err,zdata) => {
                        if(err){
                            if(callback)
                                callback(err,this)
                            else
                                throw err;
                        }
                        if(callback ){
                            callback(undefined,zdata);
                        }
                    });
                });
            } else{
                if(callback)
                    callback(new Error("Cannot extract file: Unknown compression scheme"),undefined)
                else
                    throw new Error("Cannot extract file: Unknown compression scheme");
            }
        });
    }
}


function create(fname,callback){
    new Zip(fname,callback);
}

module.exports.open = create;
