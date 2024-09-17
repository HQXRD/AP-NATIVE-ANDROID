package com.xtree.live.ui.main.model.anchorList;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * 主播助理聊天历史数据Model[主播助理IMBean]
 * 来源:/api/chat/history
 *
 *
 */
public class ChatHistoryModel extends BindModel {
    public  String id;//数据id
    public  String action;//回复状态
    public  String avatar;//頭像
    public  String blockchannles;//需要被封锁的渠道
    public  String block_channel;//需要屏蔽的渠道ID
    public  String channel;//渠道ID
    public  String channel_code;//渠道代号
    public  String channel_id;//后台专门发渠道信息用的
    public  String color;//颜色代码
    public  String creation_time;//创建时间
    public  String designation;//头衔
    public  String designation_color;//头衔颜色
    public  String exp;//用户等级
    public  String ia;//是否管理员消息(1:是,0:否)
    public  String is_batch;// 	是否后台批量发送的消息(1:是,0:否)
    public  String isVir;//是否为虚拟房间(1:是,0:否)
    public  String has_tag;// 	是否有标签(1:是,0:否)
    public  String title;//标题
    public  String link;//鏈结
    public  String method;//chat swoole 要用到的 method
    public  String server;// 	chat swoole 要用到的 server
    public  String msg_id;//讯息id
    public  String msg_type;// 	讯息类型
    public  String originalText;//原始内容
    public  String note;//备注
    public  String text;//讯息内容
    public  String pic;// 	图片地址
    public  String pic_bnc;//校正的图片地址
    public  String pm_source_type;// 	发言来源类型 [0-未知 1-直播间私聊 2-直播间聊天列表 3-聊天列表 4-赛事播放间聊天列表]
    public  String pm_source_type_str;//发言来源类型文字说明
    public  String readed_count ;//已读未读标记
    public  String room_type;//房间类型 (0 - 直播间列表; 1 - 群组聊天列表; 2 - 私聊列表)
    public  String seed;// 	唯一识别
    public  String sender;// 	消息发送者ID
    public  String sender_nickname;//消息发送者的暱称
    public  String sender_user_type;//消息发送者的类型

    public  String sender_exp;//用户等级
    public  String sender_exp_icon;//消息发送者的经验图标
    public  String sender_current_exp ;//要提升下个等级所需要的总经验量
    public  String sender_difference;//消息发送者现在等级经验到下个等级所需要的经验量
    public  String time;// 	 	时间
    public  String time_ms;// 时间戳
    public  String user_id;//助手帐户ID
    public  String username;//助手帐户名称
    public  String user_nickname;//助手帐户暱称
    public  String vid;// 聊天室识别ID(与直播间VID不同)
    public  String welcome;//是否欢迎语(1:是,0:否)

    @Override
    public String toString() {
        return "ChatHistoryModel{" +
                "id='" + id + '\'' +
                ", action='" + action + '\'' +
                ", avatar='" + avatar + '\'' +
                ", blockchannles='" + blockchannles + '\'' +
                ", block_channel='" + block_channel + '\'' +
                ", channel='" + channel + '\'' +
                ", channel_code='" + channel_code + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", color='" + color + '\'' +
                ", creation_time='" + creation_time + '\'' +
                ", designation='" + designation + '\'' +
                ", designation_color='" + designation_color + '\'' +
                ", exp='" + exp + '\'' +
                ", ia='" + ia + '\'' +
                ", is_batch='" + is_batch + '\'' +
                ", isVir='" + isVir + '\'' +
                ", has_tag='" + has_tag + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", method='" + method + '\'' +
                ", server='" + server + '\'' +
                ", msg_id='" + msg_id + '\'' +
                ", msg_type='" + msg_type + '\'' +
                ", originalText='" + originalText + '\'' +
                ", note='" + note + '\'' +
                ", text='" + text + '\'' +
                ", pic='" + pic + '\'' +
                ", pic_bnc='" + pic_bnc + '\'' +
                ", pm_source_type='" + pm_source_type + '\'' +
                ", pm_source_type_str='" + pm_source_type_str + '\'' +
                ", readed_count='" + readed_count + '\'' +
                ", room_type='" + room_type + '\'' +
                ", seed='" + seed + '\'' +
                ", sender='" + sender + '\'' +
                ", sender_nickname='" + sender_nickname + '\'' +
                ", sender_user_type='" + sender_user_type + '\'' +
                ", sender_exp='" + sender_exp + '\'' +
                ", sender_exp_icon='" + sender_exp_icon + '\'' +
                ", sender_current_exp='" + sender_current_exp + '\'' +
                ", sender_difference='" + sender_difference + '\'' +
                ", time='" + time + '\'' +
                ", time_ms='" + time_ms + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", vid='" + vid + '\'' +
                ", welcome='" + welcome + '\'' +
                '}';
    }
}
