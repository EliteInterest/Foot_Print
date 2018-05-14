//
//  ZJHttpClient.h
//  Footprint
//
//  Created by dengguichuan on 2017/11/6.
//  Copyright © 2017年 dgc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ZJFootPointModel.h"
#import "ZJFootLineModel.h"

@interface ZJHttpClient : NSObject

+(ZJHttpClient *)client;

-(void)userInfo:(NSString *)userId success:(void(^)(NSDictionary *userInfo))success error:(void(^)(NSError *error))error;

-(void)saveFootpoint:(ZJFootPointModel *)footPoint success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)saveFootline:(ZJFootLineModel *)footline success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)getFootpoints:(NSString *)userId success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)getFootlines:(NSString *)userId success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)myScore:(NSString *)userId success:(void(^)(NSDictionary *score))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)checkPhoneCode:(NSString *)phone success:(void(^)(NSDictionary *score))successHandle error:(void(^)(NSError *error))errorHandle;
    
-(void)deleteFootpoint:(NSString *)footprintid success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;
    
-(void)shareUserId:(NSString *)userid type:(NSInteger)type  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)uploadHeadImage:(UIImage *)image userId:(NSString *)userid success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;


-(void)editUsername:(NSString *)username userid:(NSString *)userid  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;
-(void)editNickname:(NSString *)nickname  userid:(NSString *)userid  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;
-(void)editPhone:(NSString *)phone code:(NSString *)code  userid:(NSString *)userid  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;

-(void)userInfoDetail:(NSString *)userid success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle;
@end
