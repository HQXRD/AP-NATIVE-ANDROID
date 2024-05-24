package com.xtree.mine.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.xtree.mvvmhabit.http.BaseResponse2;

/*【魔域】银行卡提款 信息展示bean*/
public class BankCardCashVo extends BaseResponse2 {

    public int networkStatus;//1 网络链接超时 ；2 网络链接异常 ；0 网络链接正常

    /*"nextcontroller": "security",
	"nextaction": "platwithdraw",
	"ur_here": "资金密码检查",
	异常情况 需要弹出资金密码检查
	*/
    public String nextcontroller;
    public String nextaction;
    public String ur_here;
    public String exchangerate;
    public String withdraw_rand_on;
    public int freeWithDrawTimes;
    public int usdtfee;
    public String ourfee;

    public int times;//每日限制提款次数
    public String count;//已提款次数
    //"{"6":[100,100000],"7":[100,100000],"23":[100,50000],"10":[100,100000]}"
    public String limitarr;
    public int d_max_money;
    public int d_min_money;
    public int max_money;
    public int min_money;
    public int usdt_type;
    public int channel_list_use;
    public double availablebalance;//当前可提款额度
    public Wraptime wraptime;//提款时间
    public ArrayList<ChanneBankVo> banks;//支持的银行卡列表

    public ArrayList<ChannelVo> channel_list = new ArrayList<>();//头部选项卡
    public String check;
    public String rest;//今日剩余提款额度
    public String desK;
    public String topprizes_wintips_enabled;
    public String topprizes_publicity_enabled;
    public String topprizes_herolist_enabled;
    public String user_channel_id;
    public String pub_channel_id;
    public String pub_channel_token;

    public BankCardCashVo.UserInfo user;

    /**
     * 用户信息  用户名【username】 提款类型 可提款金额【availablebalance】
     */
    public static class UserInfo {
        public String nickname;
        public String userid;
        public String username;
        public double availablebalance;
        public String registertime;
        public String relavailablebalance;
        public String formula;
        public String cafAvailableBalance;
        public int unSportActivityAward;

        public UserInfo() {

        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "userid='" + userid + '\'' +
                    ", username='" + username + '\'' +
                    ", availablebalance=" + availablebalance +
                    ", registertime='" + registertime + '\'' +
                    ", relavailablebalance='" + relavailablebalance + '\'' +
                    ", formula='" + formula + '\'' +
                    ", cafAvailableBalance=" + cafAvailableBalance +
                    ", unSportActivityAward=" + unSportActivityAward +
                    '}';
        }
        /*
        * "userid":"2788502",
        "username":"sanke35@as",
        "availablebalance":4108.69,
        "registertime":"2023-01-27 15:53:34",
        "relavailablebalance":"4108.6984",
        "formula":"(4108.69)",
        "cafAvailableBalance":4108.69,
        "unSportActivityAward":0
       */
    }

    /**
     * 提款时间
     */
    public static class Wraptime {
        public String starttime;
        public String endtime;
    }

    /**
     * 提款银行卡信息
     */
    public static class ChanneBankVo {
        @Override
        public String toString() {
            return "ChanneBankVo{" +
                    "id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", email='" + email + '\'' +
                    ", bank_id='" + bank_id + '\'' +
                    ", bank_name='" + bank_name + '\'' +
                    ", province_id='" + province_id + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", branch='" + branch + '\'' +
                    ", account_name='" + account_name + '\'' +
                    ", account='" + account + '\'' +
                    ", status='" + status + '\'' +
                    ", utime='" + utime + '\'' +
                    ", atime='" + atime + '\'' +
                    ", is_difname='" + is_difname + '\'' +
                    ", lavewdrawnum='" + lavewdrawnum + '\'' +
                    ", min_money='" + min_money + '\'' +
                    ", max_money='" + max_money + '\'' +
                    '}';
        }

        public String id;
        public String nickname;
        public String user_id;
        public String email;
        public String bank_id;
        public String bank_name;
        public String province_id;
        public String province;
        public String city;
        public String branch;
        public String account_name;
        public String account;
        public String status;
        public String utime;
        public String atime;
        public String is_difname;
        public String lavewdrawnum;
        public String min_money;
        public String max_money;
        /*
        "id":"1283067",
            "nickname":"",
            "user_id":"2788502",
            "user_name":"sanke35@as",
            "email":null,
            "bank_id":"1",
            "bank_name":"招商银行",
            "province_id":"9",
            "province":"江苏",
            "city_id":"169",
            "city":"常州",
            "branch":"啊实打实的",
            "account_name":"电饭锅",
            "account":"************3140",
            "status":"1",
            "utime":"2023-10-20 15:32:08",
            "atime":"2023-10-20 15:32:08",
            "is_difname":0,
            "lavewdrawnum":"",
            "min_money":33,
            "max_money":680000
         */

    }

    /**
     * 提款渠道
     */
    public static class ChannelVo {
        public int isShowErrorView;//1 展示错误提示信息 2展示数据信息
        public int isWebView;//1 是需要展示web网页 ; 2 需要展示原生页面
        //是否选中标志位 true 被选中；false 未被选中
        public boolean flag;
        public String typenum;
        public String name;
        public int thiriframe_use;
        public int thiriframe_status;
        public String thiriframe_msg;//顶部提现消息[银行维护中，请联系客服]
        public String thiriframe_url;//第三方展示网页
        //        public int channel_amount_use;
        public int fixamount_list_status; // 1状态表示有多个金额选择按钮 ;0状态表示没有多金额选择按钮
        public Object fixamount_list;//多个金额选择按钮 "[]", ["100", "200"]
        public List<String> fixamountList = new ArrayList<>(); // 自己加的
        //        public int fixamount_list_status1 ;
//        public String[] fixamount_list1 ;
//        public int fixamount_rule_status ;
//        public ArrayList<Object>fixamount_rule ;
//        public int earnest_money_pl ;
//        public ArrayList<Object> didvided_list ;
//        public int opfast_min_money ;
//        public int opfast_max_money ;
        public String fee_ratio;//费用比例
        //        public String configkey ;
        public int min_money;
        public int max_money;
//        public String recommend;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BankCardCashVo.ChannelVo channelVo = (BankCardCashVo.ChannelVo) o;
            return flag == channelVo.flag && thiriframe_use == channelVo.thiriframe_use && thiriframe_status == channelVo.thiriframe_status && fixamount_list_status == channelVo.fixamount_list_status && min_money == channelVo.min_money && max_money == channelVo.max_money && Objects.equals(typenum, channelVo.typenum) && Objects.equals(name, channelVo.name) && Objects.equals(thiriframe_url, channelVo.thiriframe_url) && Objects.equals(fixamount_list, channelVo.fixamount_list) && Objects.equals(fixamountList, channelVo.fixamountList) && Objects.equals(fee_ratio, channelVo.fee_ratio);
        }

        @Override
        public int hashCode() {
            return Objects.hash(flag, typenum, name, thiriframe_use, thiriframe_status, thiriframe_url, fixamount_list_status, fixamount_list, fixamountList, fee_ratio, min_money, max_money);
        }

        @Override
        public String toString() {
            return "ChannelVo{" +
                    "isShowErrorView=" + isShowErrorView +
                    ", isWebView=" + isWebView +
                    ", flag=" + flag +
                    ", typenum='" + typenum + '\'' +
                    ", name='" + name + '\'' +
                    ", thiriframe_use=" + thiriframe_use +
                    ", thiriframe_status=" + thiriframe_status +
                    ", thiriframe_msg='" + thiriframe_msg + '\'' +
                    ", thiriframe_url='" + thiriframe_url + '\'' +
                    ", fixamount_list_status=" + fixamount_list_status +
                    ", fixamount_list=" + fixamount_list +
                    ", fixamountList=" + fixamountList +
                    ", fee_ratio='" + fee_ratio + '\'' +
                    ", min_money=" + min_money +
                    ", max_money=" + max_money +
                    '}';
        }

        public ChannelVo() {

        }

        /*
         "typenum":"5",
            "name":"固额提现",
            "thiriframe_use":0,
            "thiriframe_status":0,
            "thiriframe_msg":"",
            "thiriframe_url":"",
            "channel_amount_use":0,
            "fixamount_list_status":1,
            "fixamount_list":[
                "100",
                "200",
                "300",
                "500",
                "1000",
                "2000",
                "3000",
                "5000",
                "10000",
                "20000"
            ],
            "fixamount_list_status1":0,
            "fixamount_list1":"[]",
            "fixamount_rule_status":0,
            "fixamount_rule":"[]",
            "earnest_money_pl":0,
            "didvided_list":"[]",
            "opfast_min_money":0,
            "opfast_max_money":0,
            "fee_ratio":"",
            "configkey":"hipayht",
            "min_money":100,
            "max_money":20000,
            "recommend":"0"
        */

    }
    /*
    * {
    "webtitle":"亚体游戏平台",
    "sSystemImagesAndCssPath":"",
    "customer_service_link":"https://www.tyughj.bar/service/chatlink.html",
    "pt_download_pc":"http://cdn.vbet.club/happyslots/d/setupglx.exe",
    "user":{
        "userid":"2788502",
        "username":"sanke35@as",
        "availablebalance":4108.69,
        "registertime":"2023-01-27 15:53:34",
        "relavailablebalance":"4108.6984",
        "formula":"(4108.69)",
        "cafAvailableBalance":4108.69,
        "unSportActivityAward":0
    },
    "push_service_status":1,
    "push_service_module":"{\"push_issuetime\":\"1\",\"push_issuecode\":\"1\",\"push_notice\":\"0\",\"push_usermessage\":\"1\",\"push_userbalance\":\"1\",\"push_userwonprize\":\"1\"}",
    "push_server_host":"apre3push.oxldkm.com",
    "pub_channel_token":"33c76f70561aa6753c8ff6549d3b296d",
    "pub_channel_id":"3bb42e1dd900147b89ffa2",
    "user_channel_id":"2a103f11bdefa2e48a46f5",
    "topprizes_herolist_enabled":"1",
    "topprizes_publicity_enabled":"1",
    "topprizes_wintips_enabled":"1",
    "desK":"LhRlsZpk40wHdi5BDq9reFhdzbZbVn94",
    "rest":"59544.00",
    "wraptime":{
        "starttime":"00:01",
        "endtime":"00:00"
    },
    "check":"1",
    "sSystemMessagesByTom":"SQL Info: 0 in 0 Sec, Memory Info: 5.468 MB, System Spend : 2.471911 Sec ",
    "banks":[
        {
            "id":"1283067",
            "nickname":"",
            "user_id":"2788502",
            "user_name":"sanke35@as",
            "email":null,
            "bank_id":"1",
            "bank_name":"招商银行",
            "province_id":"9",
            "province":"江苏",
            "city_id":"169",
            "city":"常州",
            "branch":"啊实打实的",
            "account_name":"电饭锅",
            "account":"************3140",
            "status":"1",
            "utime":"2023-10-20 15:32:08",
            "atime":"2023-10-20 15:32:08",
            "is_difname":0,
            "lavewdrawnum":"",
            "min_money":33,
            "max_money":680000
        },
        {
            "id":"1283066",
            "nickname":"",
            "user_id":"2788502",
            "user_name":"sanke35@as",
            "email":null,
            "bank_id":"30",
            "bank_name":"交通银行",
            "province_id":"23",
            "province":"安徽",
            "city_id":"194",
            "city":"宿州",
            "branch":"啊实打实的的",
            "account_name":"电饭锅",
            "account":"***************4998",
            "status":"1",
            "utime":"2023-10-20 15:27:58",
            "atime":"2023-10-20 15:27:58",
            "is_difname":0,
            "lavewdrawnum":"",
            "min_money":33,
            "max_money":680000
        },
        {
            "id":"1283038",
            "nickname":"",
            "user_id":"2788502",
            "user_name":"sanke35@as",
            "email":null,
            "bank_id":"69",
            "bank_name":"西安银行",
            "province_id":"1",
            "province":"北京",
            "city_id":"34",
            "city":"北京",
            "branch":"啊实打实的",
            "account_name":"电饭锅",
            "account":"************4038",
            "status":"1",
            "utime":"2023-10-10 16:37:19",
            "atime":"2023-10-10 16:37:19",
            "is_difname":0,
            "lavewdrawnum":"",
            "min_money":33,
            "max_money":680000
        },
        {
            "id":"1282872",
            "nickname":"",
            "user_id":"2788502",
            "user_name":"sanke35@as",
            "email":null,
            "bank_id":"10",
            "bank_name":"中国农业银行",
            "province_id":"13",
            "province":"吉林",
            "city_id":"126",
            "city":"延边自治州",
            "branch":"asdasdaasdasd啊实打实的",
            "account_name":"电饭锅",
            "account":"***************6246",
            "status":"1",
            "utime":"2023-08-22 15:27:40",
            "atime":"2023-08-22 15:27:40",
            "is_difname":0,
            "lavewdrawnum":"",
            "min_money":33,
            "max_money":800
        },
        {
            "id":"1282692",
            "nickname":"",
            "user_id":"2788502",
            "user_name":"sanke35@as",
            "email":null,
            "bank_id":"38",
            "bank_name":"青岛银行",
            "province_id":"17",
            "province":"四川",
            "city_id":"391",
            "city":"巴中",
            "branch":"电饭锅梵蒂冈",
            "account_name":"电饭锅",
            "account":"**************5345",
            "status":"1",
            "utime":"2023-05-26 17:19:33",
            "atime":"2023-05-26 17:19:33",
            "is_difname":0,
            "lavewdrawnum":"",
            "min_money":33,
            "max_money":680000
        }
    ],
    "availablebalance":4108.69,
    "channel_list_use":1,
    "channel_list":[
        {
            "typenum":"4",
            "name":"通用提现",
            "thiriframe_use":1,
            "thiriframe_status":1,
            "thiriframe_msg":"",
            "thiriframe_url":"https://pre-infogen.1-pay.co/op1/auction/withdraw/eyJpdiI6Im44dEZKNFlCVzFOWWZnQUtxQ1RDdXc9PSIsInZhbHVlIjoicmV6YWh0Ri9RUkdCVnArOEorSE5hV2V2N1lObTU1L3BIMkRldFBBZmowUktBWGpac1BsZExXTkViSU1JVGZLMldOSVp1YUQ3U2hiMXFBL05YQjJPQk8ybmpOMUpVK2lUUnZqS1pySFBOUkdqUS9nNm1jSGRERFoySVZNSGhFTHNNU3pja0Q0enc5akZRbFpNell2a0trb25YTlVrTmJpRXhHSU9GWmVSNUR4S0xURWxDeTI5eEFYNnRtREV5a00yIiwibWFjIjoiMDk5YWVkYWE4MDA0ZGVjZDQ4NDk5MWMyMDAzZDU3OTFmZWQ2YTZjMzFhZmM3NWU1Yzk5YWU4NGY5ZjNkNzNlMiIsInRhZyI6IiJ9",
            "channel_amount_use":0,
            "fixamount_list_status":0,
            "fixamount_list":"[]",
            "fixamount_list_status1":0,
            "fixamount_list1":"[]",
            "fixamount_rule_status":0,
            "fixamount_rule":"[]",
            "earnest_money_pl":0,
            "didvided_list":"[]",
            "opfast_min_money":0,
            "opfast_max_money":0,
            "fee_ratio":0.2,
            "configkey":"onepayfast3",
            "min_money":33,
            "max_money":680000,
            "recommend":"1"
        },
        {
            "typenum":"7",
            "name":"通用提现4",
            "thiriframe_use":1,
            "thiriframe_status":1,
            "thiriframe_msg":"",
            "thiriframe_url":"https://pre-infogen.1-pay.co/op1/auction/withdraw/eyJpdiI6IlFRbjRRUWxUazV2Zkp0NTdvcDRPSGc9PSIsInZhbHVlIjoiT1psSkRDbTQ2bEhUb1IxeTBUbmFyeG54dkVac3NtK3ZQMnJndFd0S0lWRnR0N3RHL3BhU3Y2djNrWVluWHZNeVJFRlN6QnFjV2ZCWC92WG1mcnpuWHhQZjJ3WlNNT3NQelhxTGJvZFRROTVnVzhoZ2FDRW54NTFjS3k3RjZmTWlFUzRuMllTTzVQaXQySi9rYkFSNG1QL0d6dllKOVRVc05MN1dScFZEZ3ZNRUtHRE9pT1dwelh4c2syMWpZZ0Y0IiwibWFjIjoiZDdlZTE5ODkxODY0MzQ4NjU1Njc2ZWExZjJkMDcxZjY4ZDJhZjVmZDAxZmY3NThjMWM3OTA2NzQ2ODIwMTE1MSIsInRhZyI6IiJ9",
            "channel_amount_use":0,
            "fixamount_list_status":0,
            "fixamount_list":"[]",
            "fixamount_list_status1":0,
            "fixamount_list1":"[]",
            "fixamount_rule_status":0,
            "fixamount_rule":"[]",
            "earnest_money_pl":0,
            "didvided_list":"[]",
            "opfast_min_money":0,
            "opfast_max_money":0,
            "fee_ratio":0.2,
            "configkey":"onepayfast4",
            "min_money":33,
            "max_money":680000,
            "recommend":"1"
        },
        {
            "typenum":"1",
            "name":"大额提现3",
            "thiriframe_use":0,
            "thiriframe_status":0,
            "thiriframe_msg":"",
            "thiriframe_url":"",
            "channel_amount_use":0,
            "fixamount_list_status":0,
            "fixamount_list":"[]",
            "fixamount_list_status1":0,
            "fixamount_list1":"[]",
            "fixamount_rule_status":0,
            "fixamount_rule":"[]",
            "earnest_money_pl":0,
            "didvided_list":"[]",
            "opfast_min_money":0,
            "opfast_max_money":0,
            "fee_ratio":"",
            "configkey":"generalchannel",
            "min_money":33,
            "max_money":680000,
            "recommend":"0"
        },
        {
            "typenum":"5",
            "name":"固额提现",
            "thiriframe_use":0,
            "thiriframe_status":0,
            "thiriframe_msg":"",
            "thiriframe_url":"",
            "channel_amount_use":0,
            "fixamount_list_status":1,
            "fixamount_list":[
                "100",
                "200",
                "300",
                "500",
                "1000",
                "2000",
                "3000",
                "5000",
                "10000",
                "20000"
            ],
            "fixamount_list_status1":0,
            "fixamount_list1":"[]",
            "fixamount_rule_status":0,
            "fixamount_rule":"[]",
            "earnest_money_pl":0,
            "didvided_list":"[]",
            "opfast_min_money":0,
            "opfast_max_money":0,
            "fee_ratio":"",
            "configkey":"hipayht",
            "min_money":100,
            "max_money":20000,
            "recommend":"0"
        }
    ],
    "usdt_type":1,
    "min_money":33,
    "max_money":680000,
    "d_min_money":33,
    "d_max_money":680000,
    "limitarr":"{"6":[10,550],"7":[3,100],"23":[100,50000],"10":[10,800]}",
    "count":"2",
    "times":111,
    "ur_here":"账户提款",
    "ourfee":"0",
    "usdtfee":0,
    "freeWithDrawTimes":0,
    "withdraw_rand_on":"0",
    "exchangerate":"7.2"
}
    * */

}
