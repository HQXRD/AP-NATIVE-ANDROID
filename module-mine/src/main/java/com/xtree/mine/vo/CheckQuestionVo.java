package com.xtree.mine.vo;

/*输入密保 返回 bean*/
public class CheckQuestionVo {
/*"msg_type": 2,
        "message": "验证失败"

        "status": 1,
	"msg": "",
	"data": {
		"controller": "user",
		"action": "bindsequestion",
		"accessToken": "8wrystvIln2xFsr5s9lR8TMpPJ1kmx4f",
		"quesCheck": "207"
	},
	"servertime": "2024-03-18 14:32:32",
	"ts": 0*/
    public String  msg_type ;
    public String message ;
    public Question data ;


    public class Question{
        public String  controller ;
        public String action ;
        public String accessToken ;
        public String quesCheck;

    /*"controller": "user",
		"action": "bindsequestion",
		"accessToken": "8wrystvIln2xFsr5s9lR8TMpPJ1kmx4f",
		"quesCheck": "207"*/
    }

}
