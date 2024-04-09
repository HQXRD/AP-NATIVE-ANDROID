package com.xtree.mine.vo;

import java.util.ArrayList;
import java.util.List;

public class MemberManagerVo {
    public boolean isshow; // true 显示是否下级转账
    public List<UserVo> bread; // 本级
    public MemberPageVo mobile_page;
    public ArrayList<MemberUserInfoVo> users;

    public static class UserVo {
        public String userid; // 本级
        public String username; // 本级
    }
}
