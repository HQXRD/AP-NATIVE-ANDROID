package com.xtree.live.ui.main.model.anchorList;

import com.xtree.base.mvvm.recyclerview.BindModel;

import java.util.ArrayList;

/**
 * 聊天房列表Model
 * ESCRIPTION
 *
 * 聊天室列表
 *
 * 数据来源1：透過 主播私聊接口 与主播说一次话
 *
 * 数据来源2：使用 搜索主播助手 找查助手， 并用 助手私聊 与助手说一次话
 */
public class ChatRoomModel extends BindModel {
    public String vid;//聊天室识别ID(与直播间VID不同)
    public String name;//助手暱稱
    public String room_type;//房间类型 (0 - 直播间列表; 1 - 群组聊天列表; 2 - 私聊列表)
    public String pic;//图片地址
    public String is_favorite;//是否收藏 0:否 1:是
    public String anchor_id;//主播ID
    public String anchor_nickname;//主播暱称
    public String addtime;//添加时间戳
    public String updatetime;// 	更新时间戳
    public String is_pin;//是否置顶 0:否 1:是
    public String pin_time;//置顶时间戳

    public String user_id;//助手帐户ID
    public String is_online;//是否在线 0:否 1:是
    public String is_redbag;//  	是否有正在进行的红包活动 0:否 1:是
    public String online_count;//群组的总人数跟在线人数(限后台可看)
    public String order_key;// 	消息创建时间
    public ArrayList<LastMsg>last_msg;//最后一则讯息

    public class LastMsg{
        public String vid;//聊天室识别ID(与直播间VID不同)
        public String room_type;//房间类型 (0 - 直播间列表; 1 - 群组聊天列表; 2 - 私聊列表)
        public String sender;//消息发送者ID(如果是用户本身则为0)
        public String sender_nickname;// 	消息发送者的暱称(如果是用户本身则为空)
        public String seed;//唯一识别
        public String creation_time;//消息创建时间
        public String send_at_ms;//消息发送时间戳
        public String text;//消息内容
        public String pic;//消息图片路径
        public String pic_bnc;
        public String is_batch;

    }

}
