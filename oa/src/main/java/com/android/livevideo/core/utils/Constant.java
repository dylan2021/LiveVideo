/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.livevideo.core.utils;

/**
 * @author Gool Lee
 */
public class Constant {
    private static final boolean IS_OFFICIAL = false;//正式服务器

    public static final String WEB_SITE_OFFICIAL = "http://api.oa.gztongzhiyuan.com";//正式
    public static final String WEB_SITE_TEST = "http://106.58.168.63:58822/v";//测试
    public static final String WEB_SITE = IS_OFFICIAL ? WEB_SITE_OFFICIAL : WEB_SITE_TEST;

    public static final String WEB_FILE_SEE_OFFICIAL = "http://file.gztongzhiyuan.com/";
    public static final String WEB_FILE_SEE_TEST = "http://file.escrm.waylinkage.com/";
    public static final String WEB_FILE_SEE = IS_OFFICIAL ? WEB_FILE_SEE_OFFICIAL : WEB_FILE_SEE_TEST;
    public static final String WEB_FILE_UPLOAD = WEB_SITE + "/file/files";
    public static final String CONFIG_FILE_NAME = "tzy_oa_manager.config";
    public static final String SP_TOKEN = "Token";
    public static final String CONFIG_NICK_NAME = "NickName";
    public static final String sp_pwd = "PassWord";
    public static final String CONFIG_HEAD_PHONE = "HeadUrl";
    public static final String CFG_RECEIVE_MSG = "ReceiveMsg";
    public static final String CFG_DELETE_APK = "DeleteApk";
    public static final String CFG_ALLOW_4G_LOAD = "AllowLoadBy4G";
    public static final String URL_USER_LOGIN = "/ai/user/login";
    public static final String URL_USER_REGISTER = "/user/userRegistration";
    public static final String DICT_STATUS_USED = "1";
    public static final int NET_STATUS_DISCONNECT = 0x0010;//网络未连接
    public static final int NET_STATUS_4G = 0x0011;// 4G状态连接
    public static final int NET_STATUS_WIFI = 0x0012;//WIFI状态
    public static final String APP_TYPE_ID_0_ANDROID = "0";
    public static final String URL_GET_AUTH_CODE = "/user/getAuthCode";
    public static final String NOTICE = "NOTICE";
    public static final String url_system_employees = "/system/employees";
    public static final String CONFIG_LOGIN_TYPE = "loginType";
    public static final String CONFIG_USER_CODE = "config_user_code";
    public static final String loginMode_Phone = "0";
    public static final String loginMode_Email = "1";
    public static final String WAGE_AUDIT = "WAGE_AUDIT";
    public static final String authType_Register = "0";

    public static final int EMPLYEE_SHOW_NUMBER = 4;
    public static final String PETTY_CASH = "PETTY_CASH";
    public static final int OBJECT_POOL_TRUE = 2;

    public static final String FILE_NAME_SD_CRAD_APP_PKGNAME = "file_name_sd_crad_app_pkgname";
    public static final String CONFIG_USER_EMAIL = "config_user_email";
    public static final String application_form = "application/x-www-form-urlencoded";
    public static final String application_json = "application/json";
    public static final String authorization = "Basic d2ViOjEyMzQ1Ng==";
    public static final String FILE_TYPE_IMG = "img";
    public static final String FILE_TYPE_DOC = "doc";
    public static final String TYPE_SEE = "TYPE_SEE";
    public static final int xiaoliaobao_proj_id = 2;
    public static final String xiaoliaobao_proj_name = "小料包";
    public static final int DRAFT = 1;
    public static final int COMMIT = 2;
    public static final String DEFT_AM = "08:00";
    public static final String DEFT_PM = "17:30";

    public static final String LEAVE = "LEAVE";
    public static final String BUSINESS_TRIP = "BUSINESS_TRIP";
    public static final String OVERTIME = "OVERTIME";
    public static final String REGULAR_WORKER = "REGULAR_WORKER";
    public static final String RECRUIT = "RECRUIT";
    public static final String DIMISSION = "DIMISSION";
    public static final String OFFICIAL_DOCUMENT = "OFFICIAL_DOCUMENT";
    public static final String REIMBURSE = "REIMBURSE";
    public static final String PURCHASE = "PURCHASE";
    public static final String PAY = "PAY";

    public static final String WEIGH = "WEIGH";//过磅
    public static final String RAW_PUT_IN_POOL = "RAW_PUT_IN_POOL";//入池单
    public static final String INGREDIENT_PUT_IN_POOL = "INGREDIENT_PUT_IN_POOL";//配料入池单
    public static final String SPRINKLE = "SPRINKLE";//回淋单

    public static final String TAKE_OUT_POOL = "TAKE_OUT_POOL";//起池单

    public static final String RAW_MATERIAL_CHECK = "RAW_MATERIAL_CHECK";//原料入池检验
    public static final String SEMIFINISHED_PRODUCT_CHECK = "SEMIFINISHED_PRODUCT_CHECK";//半成品
    public static final String PRODUCE_DEPT_ID = "4";
    public static final String WAGE_WAGEDETAIL_AUDIT = "WAGE_WAGEDETAIL_AUDIT";
    public static final String WAGE_PIECEDETAIL_AUDIT = "WAGE_PIECEDETAIL_AUDIT";
    public static final int INPUT_MAX_LENGTH = 19;
    public static final int INPUT_DOT_LENGTH = 2;
    public static final String appId="lca4d217e39e22461f";
    public static final String appSecret="e76d3d9b36e746e5999f3e3059fa4b";
}
