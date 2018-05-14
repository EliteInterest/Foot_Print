//
//  ZJInterfaceDefine.h
//  FootMark
//
//  Created by dengguichuan on 2017/10/31.
//  Copyright © 2017年 dgc. All rights reserved.
//

#ifndef ZJInterfaceDefine_h
#define ZJInterfaceDefine_h

#define BaseURL @"http://www.didiaosuo.com:8080"

//登陆地址
#define ZJLoginInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/logon"]

//注册
#define ZJRegistInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/register"]


//注册
#define ZJUserInfoInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/footprint/details/relation/info"]

//上传脚印
#define ZJSaveFootpointInfoInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/footprint/footmark/info/upload"]

//上传路线
#define ZJSaveFootlineInfoInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/footprint/route/info/upload"]

#define ZJGetFootlineInfoInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/footprint/route/list/info"]

#define ZJGetFootpointInfoInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/footprint/footmark/list/info"]

#define ZJGetScoreDetailInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/integral/details/info"]

#define ZJGetPhoneCodeInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/register/phone/code"]

#define ZJDeleteFootpointInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/footprint/delete/info"]

#define ZJShareFootpointInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/share/Integral"]

#define ZJUploadHeadImageInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/headPortraits/upload"]

#define ZJEditNicknameInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/info/upload"]

#define ZJEditUsenameInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/info/upload"]

#define ZJEditPhoneInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/phone/upload"]

#define ZJUserinfoDetailInterfaceAPI [BaseURL stringByAppendingPathComponent:@"/fsms/user/info"]









#endif /* ZJInterfaceDefine_h */
