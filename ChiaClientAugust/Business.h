//
//  Business.h
//  ClientSide
//
//  Created by TakahiroIshii on 2014-08-04.
//  Copyright (c) 2014 Chiaseed.inc. All rights reserved.
//

#import "JSONModel.h"

@interface Business : JSONModel

@property int id;
@property (strong, nonatomic)NSString * name;
@property (strong, nonatomic)NSString * phoneNumber;
@property (strong, nonatomic)NSString * stName;
@property (strong, nonatomic)NSString * stNumber;
@property (strong, nonatomic)NSString * postalcode;


//- (id)init;
//- (id)initBusinessWithId: (NSInteger) i andName: (NSString *) n andPhoneNumber:
//(NSString *) pNum andStNumber: (NSString *)sNum andStName: (NSString *) sName andPostalcode: (NSString *) pCode;
//



@end
