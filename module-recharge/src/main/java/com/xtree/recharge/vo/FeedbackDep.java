package com.xtree.recharge.vo;

public class FeedbackDep
{

      /*"id": "5165",
				"key": "d3901759",
				"money": "100.00",
				"bank_id": "166",
				"created": "2024-01-30 16:22:57",
				"currency_type": "1",
				"currency_money": "0.0000",
				"sysparam_prefix": "cryptotrchqppay1"*/
        public String id;
        public String key ;
        public String money ;
        public String bank_id ;
        public String created ;
        public String currency_type ;

        public String currency_money ;
        public String sysparam_prefix ;

    @Override
    public String toString() {
        return "FeedbackDep{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", money='" + money + '\'' +
                ", bank_id='" + bank_id + '\'' +
                ", created='" + created + '\'' +
                ", currency_type='" + currency_type + '\'' +
                ", currency_money='" + currency_money + '\'' +
                ", sysparam_prefix='" + sysparam_prefix + '\'' +
                '}';
    }
}
