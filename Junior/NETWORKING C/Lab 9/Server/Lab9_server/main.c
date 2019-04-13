#include <winsock2.h>
#include <stdio.h>
struct CHAT_USER
{
unsigned char user_active; //0 if inactive, 1 if active.
char user_name[16];
unsigned long user_address; //comes from recvfrom
unsigned short user_port; //comes from recvfrom
};

int main(int argc, char* argv[])
{
    WSADATA wsa;
    int i = 0;
    int j = 0;
    struct hostent* mHost;
    int port;
	char* mIP;
	SOCKET sock;

	struct sockaddr_in mAddress;

	struct CHAT_USER active_user_array[16]; //up to 16 active users.

	printf("Please input port: ");
	scanf("%d", &port);
	printf("\nPlease input the IP: ");
	scanf("%s", mIP);

    if (WSAStartup(MAKEWORD(2,2),&wsa) != 0)
    {
        printf("Failed. Error Code : %d",WSAGetLastError());
        return 1;
    }
    sock = socket(AF_INET, SOCK_DGRAM, 0);

    mAddress.sin_addr.S_addr = mIP;
    mAddress.sin_family = AF_INET;
    mAddress.sin_port = htons(port);

    if(bind(sock,(struct sockaddr *)&mAddress,sizeof(struct sockaddr))==-1)
    {
        printf("Error: Could not bind. Code=%d\n" , WSAGetLastError());
        return -1;
    }

    closesocket(sock);
    WSACleanup();
    return(0);
}
