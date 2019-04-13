//Chase Williams
//Lab 7

#include<stdio.h>
#include<winsock2.h>
#include "server.h"
#include <process.h>
#define RESPONSE_SIZE 5000

int main(int argc , char *argv[])
{
    WSADATA wsa;
    SOCKET s;
    struct sockaddr_in server;
    char *message , server_reply[RESPONSE_SIZE];
    int recv_size;

    //Initialize Winsock.
    printf("\nInitialising Winsock...");
    if (WSAStartup(MAKEWORD(2,2),&wsa) != 0)
    {
        printf("Failed. Error Code : %d",WSAGetLastError());
        return 1;
    }
    printf("Initialised.\n");

    //Create a socket
    if((s = socket(AF_INET , SOCK_STREAM , 0 )) == INVALID_SOCKET)
    {
        printf("Error: Could not create socket. Code=%d\n" , WSAGetLastError());
        return -1;
    }
    printf("Socket created.\n");

    //Fill in address structure.
    server.sin_addr.s_addr = inet_addr("127.0.0.1"); //address of google.com
    server.sin_family = AF_INET;
    server.sin_port = htons( 5050 );  // HTTP port

    //Connect to remote server
    if (connect(s , (struct sockaddr *)&server , sizeof(server)) < 0)
    {
        printf("Error: couldn't connect. Code=%d\n", WSAGetLastError());
        return -1;
    }
    printf("Connected\n");

    //Send request to server
    message = "GET / HTTP/1.1\r\n\r\n Send Me Something!";
    if( send(s , message , strlen(message) , 0) < 0)
    {
        printf("Error: send failed. Code=%d\n", WSAGetLastError());
        return -1;
    }

    printf("Data Sent:%s\n", message);

    //Receive a reply from the server
    if((recv_size = recv(s , server_reply , RESPONSE_SIZE , 0)) == SOCKET_ERROR)
    {
        printf("Error: recv failed. Code=%d\n", WSAGetLastError());
        return -1;
    }
    printf("Reply received.");

    //Add a NULL terminating character to make it a proper string before printing
    server_reply[recv_size] = '\0';

    //print out response
    printf(server_reply);

    //close socket
    closesocket(s);
    system("pause");
    //cleanup winsock
    WSACleanup();

    return 0;
}
