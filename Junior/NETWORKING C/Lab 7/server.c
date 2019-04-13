//Chase Williams
//Lab 7

#include "server.h"
int main(int argc , char *argv[])
{
    WSADATA wsa;
    SOCKET s,new_s;

    struct sockaddr_in my_addr;
    struct sockaddr_in their_addr;

    int their_addr_size;

    char *message , client_request[REQUEST_SIZE];

    char* possibleMessages[8] =
    {
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello I!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello Hate!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello This!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello Stupid!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello Final!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello But!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello Its!</H1></c></body></html>",
        "HTTP/1.0 200 Ok\r\n\r\n<html><body><c><H1>Hello Summer!</H1></c></body></html>"
    };

    int recv_size;
    srand(NULL);

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
    my_addr.sin_addr.s_addr = 0; //use default address
    my_addr.sin_family = AF_INET;
    my_addr.sin_port = htons( SERVER_PORT );  // HTTP port

    //bind to address
    if(bind(s,(struct sockaddr *)&my_addr,sizeof(struct sockaddr))==-1)
    {
        printf("Error: Could not bind. Code=%d\n" , WSAGetLastError());
        return -1;
    }
    printf("Socket bound.\n");

    while(1){
        //listen with a back-log of 10 pending connections.
        if(listen(s,10)==-1)
        {
            printf("Error: Could not listen. Code=%d\n" , WSAGetLastError());
            return -1;
        }
        printf("Socket listening.\n");

        //accept connections
        new_s=accept(s,(struct sockaddr*)&their_addr,&their_addr_size);
        printf("Connection accepted.\n");

        if(strstr(client_request, "/quit"))
        {
            break;
        }

        //Receive request from client
        if((recv_size = recv(new_s , client_request , REQUEST_SIZE , 0)) == SOCKET_ERROR)
        {
            printf("Error: recv failed. Code=%d\n", WSAGetLastError());
            return -1;
        }
        printf("Reply received. Bytes received = %d\n",recv_size);
        printf("Request Received: %s",client_request);



        //Send response to client
        message = possibleMessages[rand() % 8];
        if( send(new_s , message , strlen(message) , 0) < 0)
        {
            printf("Error: send failed. Code=%d\n", WSAGetLastError());
            return -1;
        }
        printf("Response Sent\n");
        printf("Response Sent: %s",message);
    }
    printf("Closing sockets cleaning-up.\n");
    //close new socket
    closesocket(new_s);

    //close socket
    closesocket(s);

    //cleanup winsock
    WSACleanup();

    printf("Sockets closed and winsock cleaned-up.\n");

    return 0;
}
