package com.xtree.mine.vo;

import java.util.List;

/**
 * 银行列表和省列表, 新绑定银行卡时用的
 */
public class UserBankProvinceVo extends UserBindBaseVo<UserBankProvinceVo.BankInfoVo> {

    //public List<BankInfoVo> banklist;
    public List<AreaVo> provincelist;
    //public String flag; // "add",
    //public String checkcode; // "ab1c***3d9a",
    //public String sSystemMessagesByTom; // ""SQL Info: 0 in 0 Sec, Memory Info: 4.534 MB, System Spend : 0.213146 Sec ",

    //public int msg_type; // 1, 失败
    //public String message; // 您的银行卡已经锁定，无法绑定新银行卡！

    public static class BankInfoVo {
        //public String id; // "222",
        //public String api_id; // "4",
        //public String api_name; // "myself",
        public String bank_id; // "206",
        public String bank_name; // "龙江银行",
        //public String bank_code; // "LJB",
        public String status; // "1",
        //public String utime; // "2023-12-12 12:29:24",
        //public String atime; // "2022-11-16 16:04:30"

        @Override
        public String toString() {
            return "BankInfoVo{" +
                    "bank_id='" + bank_id + '\'' +
                    ", status='" + status + '\'' +
                    ", bank_name='" + bank_name + '\'' +
                    '}';
        }
    }

    public static class AreaVo {
        public String id; // "2",
        //public String parent_id; // "0",
        public String name; // "上海",
        public String name1; // "Shanghai",
        //public String name2; // "上海",
        //public String name3; // "上海",
        //public String fullname; // "",
        //public String zipcode; // "200000",
        //public String telecode; // "021",
        //public String ext; // "sh",
        //public String used; // "1",
        //public String orders; // "0"

        @Override
        public String toString() {
            return "ProvinceVo{" +
                    "id='" + id + '\'' +
                    //", parent_id='" + parent_id + '\'' +
                    ", name='" + name + '\'' +
                    ", name1='" + name1 + '\'' +
                    '}';
        }
    }

}
