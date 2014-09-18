//
//  main.m
//  ClientSide
//
//  Created by Naoya on 12/29/2013.
//  Copyright (c) 2013 Chiaseed.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AsyncSocket.h"
#import "ChiaClient.h"

int main(int argc, const char * argv[])
{
    //make new client with any user name
    ChiaClient *client = [[ChiaClient alloc] initWithUseName:@"Client1"];
    
    // call connectHost functio to connect and start service with server
    [client connectHost: @"localhost" port: 8080]; // change "localhost" to the server's IP if you test with another pc
    
    return 0;
}

