//
//  ZJHttpClient.m
//  Footprint
//
//  Created by dengguichuan on 2017/11/6.
//  Copyright © 2017年 dgc. All rights reserved.
//

#import "ZJHttpClient.h"

@implementation ZJHttpClient

+(ZJHttpClient *)client
{
    static ZJHttpClient *client = nil;
    @synchronized(self){
        if (!client) {
            client = [ZJHttpClient new];
        }
    }
    return client;
}

-(void)userInfo:(NSString *)userId success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"userId":userId};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJUserInfoInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"获取个人信息失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)saveFootpoint:(ZJFootPointModel *)footPoint success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSMutableDictionary *infos = [NSMutableDictionary dictionary];
    
    NSDictionary *Footprint = @{@"UserId":[ZJUserModel curUser].uid,
                                @"Name":footPoint.addressModel.streetName,
                                @"Desc":footPoint.addressModel.address,
                                @"StartTime":@(footPoint.sTime),
                                @"EndTime":@((long)[[NSDate date] timeIntervalSince1970])
                                };
    
    [infos setObject:Footprint forKey:@"Footprint"];
    
    NSDictionary *PointPosition = @{
        @"PointId": footPoint.id,
        @"Longitude": @(footPoint.point.x),
        @"Latitude": @(footPoint.point.y),
        @"Altitude":@(footPoint.point.z),
        @"Addr": footPoint.addressModel.address,
        @"Desc": footPoint.addressModel.address,
        @"PointType": @(footPoint.type + 1)
        };
    [infos setObject:PointPosition forKey:@"PointPosition"];
    
    if (footPoint.type == ZJFootTextType) {
        NSDictionary *FileInfo =@{
          @"TextInfo":@{
                  @"TextName":footPoint.texts.name,
                  @"TextDesc":footPoint.texts.desc?:@""
                  }
          };
        [infos setObject:FileInfo forKey:@"FileInfo"];
    }
    
    if (footPoint.type == ZJFootImageType) {
        NSMutableArray *array = [NSMutableArray array];
        for (ZJImageModel *img in footPoint.images) {
            [array addObject:img.desc?:@""];
        }
        NSDictionary *FileInfo = @{
                                   @"TotalDesc":footPoint.desc?:@"",
                                   @"MediaInfo":array
                                   };
        [infos setObject:FileInfo forKey:@"FileInfo"];
    }
    
    if (footPoint.type == ZJFootVideoType) {
        
    
        
        NSDictionary *FileInfo = @{
                                    @"TotalDesc":footPoint.desc?:@"",
                                    @"MediaInfo":@[footPoint.desc?:@""]
                                   };
        [infos setObject:FileInfo forKey:@"FileInfo"];
    }
    
    NSDictionary *args =@{@"infos":[infos ags_JSONRepresentation]};    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    [manager POST:ZJSaveFootpointInfoInterfaceAPI parameters:args constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        
        if (footPoint.type == ZJFootImageType) {
            for (ZJImageModel *img in footPoint.images) {
                [formData appendPartWithFileURL:img.imageURL name:@"FileInfo" fileName:@".jpg" mimeType:@"multipart/form-data" error:nil];

            }
        }
       if (footPoint.type == ZJFootVideoType) {
           [formData appendPartWithFileURL:footPoint.video.videoURL name:@"FileInfo" fileName:@".mp4" mimeType:@"multipart/form-data" error:nil];
       }
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        if ([responseObject[@"Code"] integerValue] == 200000) {
            successHandle(responseObject[@"Data"]);
        }
        else
        {
            errorHandle([NSError errorWithDomain:@"获取个人信息失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        errorHandle(error);
    }];
}


-(void)saveFootline:(ZJFootLineModel *)footline success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSMutableDictionary *infos = [NSMutableDictionary dictionary];
    
    NSDictionary *Footprint = @{@"UserId":[ZJUserModel curUser].uid,
                                @"Name":footline.name?:@"",
                                @"Desc":footline.desc?:@"",
                                @"Mileage":@(footline.mileage),
                                @"ConsumptionTime":@(footline.duration),
                                @"StartTime":@(footline.startTimeInterval),
                                @"EndTime":@(footline.endTimeInterval)
                                };
    
    [infos setObject:Footprint forKey:@"Footprint"];
    
    NSMutableArray *PointPosition = [NSMutableArray array];
    NSMutableArray *TextInfo = [NSMutableArray array];
    NSMutableArray *MediaInfo =  [NSMutableArray array];
    for (ZJFootPointModel *footPoint in footline.footPointModels) {
        NSDictionary *point = @{
                                        @"PointId": footPoint.id,
                                        @"Longitude": @(footPoint.point.x),
                                        @"Latitude": @(footPoint.point.y),
                                        @"Altitude":@(footPoint.point.z),
                                        @"Addr": footPoint.addressModel.address,
                                        @"Desc": footPoint.addressModel.address,
                                        @"PointType": @(footPoint.type + 1)
                                        };
        [PointPosition addObject:point];
        
        if (footPoint.type == ZJFootTextType) {
            NSDictionary *FileInfo =@{
                                      @"PointId":footPoint.id,
                                      @"TextName":footPoint.texts.name,
                                      @"TextDesc":footPoint.texts.desc?:@""
                                      };
            [TextInfo addObject:FileInfo];
        }
        
        if (footPoint.type == ZJFootImageType) {
            NSMutableArray *array = [NSMutableArray array];
            for (ZJImageModel *img in footPoint.images) {
                [array addObject:img.desc?:@""];
            }
            NSDictionary *FileInfo = @{
                                       @"PointId":footPoint.id,
                                       @"TotalDesc":footPoint.desc?:@"",
                                       @"EachDesc":array
                                       };
            [MediaInfo addObject:FileInfo];
        }
        
        if (footPoint.type == ZJFootVideoType) {
            
            
            
            NSDictionary *FileInfo = @{
                                       @"PointId":footPoint.id,
                                       @"TotalDesc":footPoint.desc?:@"",
                                       @"EachDesc":@[footPoint.desc]
                                       };
            [MediaInfo addObject:FileInfo];
        }
    }
    
    NSMutableArray *PathInfo = [NSMutableArray array];
    for (AGSPoint *point in footline.line) {
        [PathInfo addObject:@[@(point.x),@(point.y),@(point.z)]];
    }
    
    
    [infos setObject:PointPosition forKey:@"PointPositions"];
    
//    NSDictionary *args =@{
//                          @"FootprintInfo":[infos ags_JSONRepresentation],
//                          @"TextInfo":[@{@"TextInfo":TextInfo} ags_JSONRepresentation],
//                          @"PathInfo":[PathInfo ags_JSONRepresentation],
//                          @"SaveInfo":[@{@"MediaInfo":MediaInfo} ags_JSONRepresentation]
//                          };
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    [manager POST:ZJSaveFootlineInfoInterfaceAPI parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        
        NSData *data1 = [[infos ags_JSONRepresentation] dataUsingEncoding:NSUTF8StringEncoding];
        [formData appendPartWithFormData:data1 name:@"FootprintInfo"];

        NSData *data2 = [[@{@"TextInfo":TextInfo} ags_JSONRepresentation] dataUsingEncoding:NSUTF8StringEncoding];
        [formData appendPartWithFormData:data2 name:@"TextInfo"];

        NSData *data3 = [[PathInfo ags_JSONRepresentation] dataUsingEncoding:NSUTF8StringEncoding];
        [formData appendPartWithFormData:data3 name:@"PathInfo"];

        NSData *data4 = [[@{@"MediaInfo":MediaInfo} ags_JSONRepresentation] dataUsingEncoding:NSUTF8StringEncoding];
        [formData appendPartWithFormData:data4 name:@"SaveInfo"];
        
        if (footline.thumbnail) {
            NSData *data = UIImageJPEGRepresentation(footline.thumbnail, 1.0);
            [formData appendPartWithFileData:data name:@"FileInfo" fileName:@".jpg" mimeType:@"multipart/form-data"];
        }
        
        for (ZJFootPointModel *footPoint in footline.footPointModels) {

        if (footPoint.type == ZJFootImageType) {
            for (ZJImageModel *img in footPoint.images) {
                [formData appendPartWithFileURL:img.imageURL name:@"FileInfo" fileName:@".jpg" mimeType:@"multipart/form-data" error:nil];

            }
        }
        if (footPoint.type == ZJFootVideoType) {
            [formData appendPartWithFileURL:footPoint.video.videoURL name:@"FileInfo" fileName:@".mp4" mimeType:@"multipart/form-data" error:nil];
        }
        }
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        if ([responseObject[@"Code"] integerValue] == 200000) {
            successHandle(responseObject[@"Data"]);
        }
        else
        {
            errorHandle([NSError errorWithDomain:@"获取个人信息失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        errorHandle(error);
    }];
}

-(void)getFootpoints:(NSString *)userId success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"userId":userId};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJGetFootpointInfoInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"获取个人信息失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)getFootlines:(NSString *)userId success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"userId":userId};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJGetFootlineInfoInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"获取个人信息失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}


-(void)myScore:(NSString *)userId success:(void(^)(NSDictionary *score))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"userId":userId};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJGetScoreDetailInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"获取积分详情失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)checkPhoneCode:(NSString *)phone success:(void(^)(NSDictionary *score))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"number":phone};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJGetPhoneCodeInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"获取积分详情失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}
    
-(void)deleteFootpoint:(NSString *)footprintid success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"footprintId":footprintid};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJDeleteFootpointInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"删除失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}
    
-(void)shareUserId:(NSString *)userid type:(NSInteger)type  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"userId":userid,@"type":@(type)};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJShareFootpointInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"上传失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)uploadHeadImage:(UIImage *)image userId:(NSString *)userid success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    [manager POST:ZJUploadHeadImageInterfaceAPI parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        
        NSData *data1 = [userid dataUsingEncoding:NSUTF8StringEncoding];
        [formData appendPartWithFormData:data1 name:@"userId"];
        
      
            NSData *data = UIImageJPEGRepresentation(image, 0.5);
            [formData appendPartWithFileData:data name:@"file" fileName:@".jpg" mimeType:@"file"];
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        if ([responseObject[@"Code"] integerValue] == 200000) {
            successHandle(responseObject[@"Data"]);
        }
        else
        {
            errorHandle([NSError errorWithDomain:@"获取个人信息失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        errorHandle(error);
    }];
}

-(void)editUsername:(NSString *)username userid:(NSString *)userid  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"UserId":userid,@"UserName":username};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager POST:ZJEditUsenameInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"上传失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)editNickname:(NSString *)nickname  userid:(NSString *)userid  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"UserId":userid,@"Nickname":nickname};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager POST:ZJEditNicknameInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"上传失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)editPhone:(NSString *)phone code:(NSString *)code  userid:(NSString *)userid  success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"UserId":userid,@"Number":phone,@"Code":code};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager POST:ZJEditPhoneInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"上传失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}

-(void)userInfoDetail:(NSString *)userid success:(void(^)(NSDictionary *userInfo))successHandle error:(void(^)(NSError *error))errorHandle
{
    NSDictionary *args =@{@"userId":userid};
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager GET:ZJUserinfoDetailInterfaceAPI parameters:args progress:nil
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             if ([responseObject[@"Code"] integerValue] == 200000) {
                 successHandle(responseObject[@"Data"]);
             }
             else
             {
                 errorHandle([NSError errorWithDomain:@"上传失败" code:[responseObject[@"Code"] integerValue] userInfo:nil]);
             }
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             errorHandle(error);
         }];
}
@end
