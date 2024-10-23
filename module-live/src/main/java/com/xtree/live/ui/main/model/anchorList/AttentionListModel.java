package com.xtree.live.ui.main.model.anchorList;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;

/**
 * 已关注主播列表  api/user/attentionList
 */

public class AttentionListModel extends BindModel {
    /*"uid": 7952,
        "followed_id": 24630,
        "attention": 42920,
        "user_nickname": "\u88ab\u5173\u6ce8\u7528\u6237\u7684\u7528\u6237\u6635\u79f0",
        "avatar": "\u88ab\u5173\u6ce8\u7528\u6237\u7684\u7528\u6237\u5934\u50cf",
        "article_num": 2537,
        "post_num": 24345,
                "is_live": 42922,
        "votestotal": 12416,
        "votestotal_icon": "\u88ab
    * */
    public int  uid;//用户id
    public int  followed_id;//被关注用户id
    public int  attention;//被关注用户的关注人数
    public String  user_nickname;//被关注用户的用户昵称
    public String  avatar;// 	被关注用户的用户头像
    public int  article_num;//被关注用户的发布文章数
    public int  post_num;//被关注用户的发布帖子数
    public int  is_live;//是否有正在直播(1:是,0:否)
    public int  votestotal;//被关注主播的主播等级(以该主播收到礼物总额计算得出)
    public String  votestotal_icon;//被关注主播的等级标识图片

    public ArrayList<Reserver> reserve ;
    public ArrayList<Live> live ;


    public class  Reserver{
        public int  id;//记录id
        public int  uid;//被关注用户id
        public int  type;//直播分类(0:足球,1:篮球,2:其他)
        public String  match_id;//比赛id
        public String  starttime;//开播时间
        public String  title;//标题
        public String  thumb;//封面图
        public int  reserve_num;//预约人数
        public String  bulletin;//公告
        public String  updatetime;//修改时间
        public String  addtime;//添加时间
        public String  deletetime;//删除时间
    }

    public class Live{
        public int  id;//记录id
        public int  uid;//被关注用户id
        public int  islive;//直播状态(-1:创建,0:未直播,1:正常,2:取消)
        public int  type;//直播分类(0:足球,1:篮球,2:其他,3:电竞,4:区块链)
        public int  match_id;//比赛id
        public String  starttime;//标题
        public String  title;//标题
        public int  stream;//流名
        public int  stream_type;//直播流类型(0:腾讯云,1:阿里云,2:华为云)
        public int  video_source_type;//赛事视频来源类型(0:纳米,1:火星)
        public int  record_task_id;//录制任务的id: live_record_task.task_id
        public String  thumb;//封面图

        public String  pull;//播流地址
        public int  pull_type;// 	流类型(0:视频流,1:网页动画)
        public String  push;// 	推流地址
        public int  hotvotes;//礼物总额
        public int  viewers;//观看人数
        public String  live_duration;//直播时长
        public int  ishot;//是否热门
        public int  isrecommend;// 	是否推荐
        public int  is_home;//推荐类别(1:推荐到首页,0:不推荐)

        public int  clicktime;//点击开播时间
        public int  closetime;// 	点击关播时间
        public String  updatetime;// 	修改时间
        public String  addtime;//添加时间
        public String  deletetime;//删除时间
        public String  bulletin;//直播公告
        public String  priority;//优先级
        public int  ip_count;//ip_count 	IP数
        public int  user_count;//用户数

        public int  v_user_count;//注水人数
        public int  v_user_multiple;//注水倍数
        public String  vid;//聊天的房间号
        public String  marquee;//滚屏公告
        public int  is_webrtc;//是否使用webrtc(0:否,1:是,2:系统预设)
        public String  live_format;//直播间影片使用格式
        public String  record_task_id_local;//本地录制任务的id: live_record.id
        public String  extra;// 	扩充栏位存放json
        public String  robot_status;// 	机器人状态(1:开启,2:关闭)

        /*"id": 54116,
                "uid": 33646,
                "islive": 15189,
                "type": 22672,
                "match_id": 48731,
                "starttime": 1729666116,
                "title": "\u6807\u9898",
                "stream": "\u6d41\u540d",
                "stream_type": 4653,
                "video_source_type": 24229,
                "record_task_id": 52541,
                "thumb": "\u5c01\u9762\u56fe",
                "pull": "\u64ad\u6d41\u5730\u5740",
                "pull_type": 17719,
                "push": "\u63a8\u6d41\u5730\u5740",
                "hotvotes": 9259,
                "viewers": 61060,
                "live_duration": "\u76f4\u64ad\u65f6\u957f",
                "ishot": 28917,
                "isrecommend": 56880,
                "is_home": 9260,
                "clicktime": 1474,
                "closetime": 44494,
                "updatetime": 1729666116,
                "addtime": 1729666116,
                "deletetime": 1729666116,
                "bulletin": "\u76f4\u64ad\u516c\u544a",
                "priority": 61578,
                "ip_count": 13980,
                "user_count": 45867,
                "v_user_count": 52475,
                "v_user_multiple": 49700,
                "vid": "\u804a\u5929\u7684\u623f\u95f4\u53f7",
                "marquee": "\u6eda\u5c4f\u516c\u544a",
                "is_webrtc": 32678,
                "live_format": "\u76f4\u64ad\u95f4\u5f71\u7247\u4f7f\u7528\u683c\u5f0f",
                "record_task_id_local": "\u672c\u5730\u5f55\u5236\u4efb\u52a1\u7684id: live_record.id",
                "extra": "\u6269\u5145\u680f\u4f4d\u5b58\u653ejson",
                "robot_status": 12865*/
    }

}
