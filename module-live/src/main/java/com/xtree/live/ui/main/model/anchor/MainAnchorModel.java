package com.xtree.live.ui.main.model.anchor;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;

/**
 * 主页获取主播列表
 */
public class MainAnchorModel extends BindModel {

    public ArrayList<User> user ;//主播信息列表
    public String total;// 	总数居量
    public String per_page;//每页数量
    public String current_page;//当前页
    public String last_page;//最后一页位置
    public String anchor_id; //主播id
    public String platform;//终端类型 1： PC; 2：H5；3：android；4：ios；
    public String side;//投放位置 1：足球；2：篮球；3：电竞；4：首页；5：直播间；6：其它；
    public String listRows;//每页数量
    public String type;//数据类型 1：热门， 2：推荐
    public String description;//说明
    public String heat;//热度
    public String is_live;//是否在直播 1：正在直播 0：不在直播

    public class  Page{
        public boolean  hasNextPage ; //是否有下一页 true 有
        public int  nowPage;//当前页
        public int pageSize ;//每页数量
        public int allCount;//所有数据量
        public ArrayList<Object> exData;//额外信息 数组:用,分隔
        public int serverTime;//整型
    }

    /**
     * 主播信息
     */
    public class  User{
        public String id; //主播id
        public String user_nickname; //主播昵称
        public String avatar ; //主播头像
        public String attention;//关注人数
    }


}
