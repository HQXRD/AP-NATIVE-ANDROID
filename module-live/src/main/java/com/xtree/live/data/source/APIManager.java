package com.xtree.live.data.source;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: api 静态管理
 */
public class APIManager {

    //直播
    private static final String X9_API = "/api/x9/";
    //获取直播token
    public static final String X9_TOKEN_URL = X9_API + "getXLiveToken";

    //聊天房列表
    private static final String CHATROOMLIST_API = "/api/chat/getChatRoomList";

}
