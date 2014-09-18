#import <Foundation/Foundation.h>
#import "AsyncSocket.h"


// header file for ChiaClient
// shows 2 instance variables and 2 extra functios that I added on AsyncSocket
@interface ChiaClient : NSObject



// do not worry about "nonatmic" and "strong" stuff
// it's just declaring
@property (nonatomic, strong) AsyncSocket *socket;
@property (nonatomic, strong) NSString *userName;


// once again just declaring 2 functio names
- (id)initWithUseName: (NSString *) name;
-(void) connectHost: (NSString *) host port: (UInt16) port;

@end