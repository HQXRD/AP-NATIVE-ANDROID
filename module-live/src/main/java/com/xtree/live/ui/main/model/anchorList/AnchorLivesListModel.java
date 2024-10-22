package com.xtree.live.ui.main.model.anchorList;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;

/**
 * 获取正在直播的列表   来源 /api/front/lives
 */
public class AnchorLivesListModel extends BindModel {

    public ArrayList<AnchorLiveModel> data;//主播列表
    public Page page ;//分页信息

    /**
     * 主播列表Bean
     */
    public class AnchorLiveModel{
        public String id;// 	总数居量
        public String vid;//房间vid
        public String uid;//用户id
        public int type;//直播分类：0足球 1篮球 2其他 3电竞 4区块链
        public int isLive; //直播状态：-1创建 0未直播 1正常 2取消，只返回返回状态为1的数据
        public int  loadingBar;//是否录播
        public String pull;//拉流地址
        public int  matchId;//比赛id
        public String title;//直播间标题
        public String thumb;//直播间封面
        public int isLoop;//没有真人主播返回true
        public String avatar;//主播头像
        public String userNickname;//用户昵称
        public String heat;//主播热度（后台-设置-网站信息-默认热度值 +（主播的v_user_count*v_user_multiple））

        @Override
        public String toString() {
            return "AnchorLiveModel{" +
                    "id='" + id + '\'' +
                    ", vid='" + vid + '\'' +
                    ", uid='" + uid + '\'' +
                    ", type=" + type +
                    ", isLive=" + isLive +
                    ", loadingBar=" + loadingBar +
                    ", pull='" + pull + '\'' +
                    ", matchId=" + matchId +
                    ", title='" + title + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", isLoop=" + isLoop +
                    ", avatar='" + avatar + '\'' +
                    ", userNickname='" + userNickname + '\'' +
                    ", heat='" + heat + '\'' +
                    '}';
        }
    }

    /**
     * 分页信息
     */
    public class  Page{
        public boolean  hasNextPage ; //是否有下一页 true 有
        public int  nowPage;//当前页
        public int pageSize ;//每页数量
        public int allCount;//所有数据量
        public ArrayList<Object> exData;//额外信息 数组:用,分隔
        public int serverTime;//整型
    }
}
