/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.app.footprint.api;

public class ApiConstants {

    public static boolean ISRELEASE = true;// 是否正式环境
    //    public static final String RELEASE_URL = ISRELEASE ? "http://zhsq.digitalcq.com/zckgpro/" : "http://192.168.21.37/zckg/";
    public static final String RELEASE_URL = ISRELEASE ? "http://www.didiaosuo.com:8080":"http://47.92.150.231:8080/";
//        public static final String SERVER_URL = "api_v3.php";
    public static final String SERVER_URL = "api_v1.do";
    public static final String LOGIN_URL = "/fsms/user/logon";
    public static final String REGISTER_URL = "/fsms/user/register";
    public static final String SEND_PHONE_URL = "/fsms/user/register/phone/code";
    public static final String FILEDOWNLOAD_URL = ISRELEASE ? "http://zhsq.digitalcq.com/zckgpro/serviceMap/" : "http://zhsq.digitalcq.com/zckgpro/serviceMap/";
}
