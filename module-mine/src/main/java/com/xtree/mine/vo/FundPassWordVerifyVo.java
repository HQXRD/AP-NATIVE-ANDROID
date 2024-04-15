package com.xtree.mine.vo;

/*输入资金密码/谷歌验证码返回bean*/
public class FundPassWordVerifyVo {
    /*返回：{
	"status": 1,
	"msg": {
		"checkcode": "3f64c55b10fa6293b87b5d737108bf53"
	},
	"data": [],
	"servertime": "2024-03-06 09:48:44",
	"ts": 0
    }*/
    //"msg_type": 2,"message": "资金密码错误"
    public String msg_type ;
    public String message ;
    public String status ;
    public FunPWMessage msg;
    public static  class FunPWMessage
    {
        public String checkcode ;//返回带到下一个接口check=3f64c55b10fa6293b87b5d737108bf53
    }
}
