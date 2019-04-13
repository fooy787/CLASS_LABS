//Chase Williams
//Lab 8
#include <winsock2.h>
#include <stdio.h>

int main(int argc, char* argv[])
{
    WSADATA wsa;
    int i = 0;
    int j = 0;
    struct hostent* mHost;
    if(argc > 2)
    {
        printf("Error! Too many things passed in!");
        return(1);
    }
    WSAStartup(MAKEWORD(2,2), &wsa);
    mHost = gethostbyname(argv[1]);

    printf("%s\n", mHost->h_name);

    while(mHost->h_aliases[i] != 0)
    {
        printf("%s\n", mHost->h_aliases[i]);
        i++;
    }
    while(mHost->h_addr_list[j] != 0)
    {
        printf("%s\n", inet_ntoa(*(struct in_addr*)mHost->h_addr_list[j]));
        j++;
    }
    return(0);
}
