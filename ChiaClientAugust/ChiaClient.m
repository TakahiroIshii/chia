//
//  ChiaClient.m
//  client_test_dec_23
//
//  Created by Takahiro Ishii on 12/23/2013.
//  Copyright (c) 2013 Takahiro Ishii. All rights reserved.
//

#import "ChiaClient.h"
#import "AsyncSocket.h"
#import "Business.h"

// this is where everybody gives up to go through, please be brave

@implementation ChiaClient


// this synthesizing may be meaning less...
@synthesize socket = _socket;
@synthesize userName = _userName;


// function to initialize ChiaClient with user name
// "id" means its return type is an object and itself
- (id)initWithUseName: (NSString *) name
{
    _userName = name;
    NSLog (@"Creating socket for User : %@\n", _userName); // just printing
    
	_socket = [[AsyncSocket alloc] initWithDelegate:self]; // allocating or making an object of AsyncSocket,
    //self means this (ChiaClient) object itself
    
	return self;
}


// function to connect to server
// after this we use functios from AsyncSocket
-(void) connectHost: (NSString *) host port: (UInt16) port
{
    
    // needs to be in try-catch so if anything goes wrong, we catch it
	@try
	{
		NSError *err;
		
		if ([_socket connectToHost:host onPort:port error:&err]) // if it connects to server without any error
        {
			NSLog (@"Connecting to IP: %@ Port: %u.\n", host, port);
            [[NSRunLoop currentRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:1.0]]; // here is the point where all service by AsyncSocket starts
        }
		else
			NSLog (@"Couldn't connect to IP: %@ Port: %u (%@).\n", host, port, err); // if it fails to connect
	}
	@catch (NSException *exception)
	{
		NSLog (@"%@\n", [exception reason]); // for any other unknown error
	}
}

//------------------------------------------------------------------------------------------------------------------------------------

#pragma mark AsyncSocket Delegate Methods
// from here, every function comes from AsyncSocket, but inside of all functions are original.
// you can guess when those functions are called by looking at names of functions.
// PLEASE follow the "tag". tag dose not change over functions.
// functions are passing tags around.





// this is the first function to get called
// this gets called ONCE, right after client connects to server
-(void) onSocket:(AsyncSocket *)sock didConnectToHost:(NSString *)host port:(UInt16)port;
{
    // just printing (somehow it prints weird host name)
	NSLog (@"Connected to %@ %u.", host, port);
    
    // now it is time to read some message from server. and tag is 0
    [self readWithTag:0];
}



// this function is actually not from AsyncSocket.
// I made it so evry time it trys to read, just use this function
// this function does not change tag
- (void)readWithTag:(long)tag {
    
	NSLog(@"Reading...\n");
    
    // calls a function from AsyncSocket to read data
    [_socket readDataToData:[AsyncSocket CRLFData] withTimeout:-1 tag:tag]; // timeout is in cecond, it it is negative means never time out
}



// this functio gets called right after client read some thing
// never gets called if client fails to read
- (void)onSocket:(AsyncSocket *)sock didReadData:(NSData *)data withTag:(long)tag {
    NSLog(@"Recieved data from server.\n");
    // to print out message from server, use UTF8 encoding sype(same as server does)
    NSString *meesageFromServer = [[NSString alloc] initWithBytes:[data bytes] length:[data length] encoding:NSUTF8StringEncoding];
    NSLog(@"Server: %@", meesageFromServer); // print out message
    
    
    // change nexe action depends on tag
    
    // tag: 0
    // send user name to server
    // change tag to 2
    if(tag == 0)  {
        NSData *data = [[_userName stringByAppendingString:@"\n"] dataUsingEncoding:NSUTF8StringEncoding];
        [_socket writeData: data withTimeout:1 tag:1];
    }
    
    // tag: 1
    // send command "0" to server
    // command has to be NSString
    // command 0 means use direct search on business names
    // change tag to 3
    else if(tag == 1)  {
        NSLog (@"Sending command \"0\"...\n");
        NSString *command = @"0\n";  // DO NOT FORGET \n !!!!!!
        NSData *data = [command dataUsingEncoding:NSUTF8StringEncoding];
        [_socket writeData: data withTimeout:1 tag:2];
    }
    
    
    // tag: 2
    // send words for searching
    else if(tag == 2)
    {
        if ([meesageFromServer  isEqual: @"ready\r\n"])
        {
            NSLog (@"Sending words...\n");
            NSString *words = @"joe\n"; // DO NOT FORGET \n !!!!!!
            NSData *data = [words dataUsingEncoding:NSUTF8StringEncoding];
            [_socket writeData: data withTimeout:1 tag:3];
        }
        else
        {
            NSLog (@"server was not ready...\n");
            [sock disconnect];
        }
    }
    
    
    // tag: 3
    // this is where a list of result business comes
    else if (tag == 3)
    {
        // may not necessary
        NSError* err = nil;
        
        // change JSON formated in put to an array
        NSArray *tempArray = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&err];
        // declare each element as Business
        NSMutableArray* resultBusinesses = [Business arrayOfModelsFromDictionaries: tempArray error:&err];
        
        
        // print out names and postalcodes for all businesses from result
        for (Business* b in resultBusinesses)
        {
            NSLog (@"name is %@\n", [b name]);
            NSLog (@"postalcode is %@\n", [b postalcode]);
        }
        
        NSData *data = [@"see you\n" dataUsingEncoding:NSUTF8StringEncoding];
        [_socket writeData: data withTimeout:1 tag:5];
    }
    
    
    // tag: 4
    // disconnect
    else if (tag == 4)
    {
        [sock disconnect];
    }
}



// this function gets called right after client send data to server
// never gets called if client fails to send
- (void)onSocket:(AsyncSocket *)sock didWriteDataWithTag:(long)tag {
    NSLog(@"Sent data to server.\n"); // just printing
    
    
    // if tag is not 5, just pass the tag and read
    
    if(tag != 5)
        [self readWithTag:tag];
    
    
    
    
    // tag: 5
    // disconnect client from server
    else if(tag == 5)  {
        [sock disconnect];
    }
}



// this function gets called, if an effor occurs
// client will disconnect and error will be printed out
-(void) onSocket:(AsyncSocket *)sock willDisconnectWithError:(NSError *)err
{
	if (err != nil)
		NSLog (@"Socket will disconnect. Error domain %@, code %ld (%@).\n",
			   [err domain], (long)[err code], [err localizedDescription]);
	else
		NSLog (@"Socket will disconnect. No error.\n");
}



// nomal disconnect function
-(void) onSocketDidDisconnect:(AsyncSocket *)sock
{
	NSLog (@"%@ is successfully Disconnected.\n", _userName);
}




@end