package me.xtree.mvvmhabit.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 老接口返回的数据基类(父类), 适合老接口用
 */
public class BaseResponse2 {

    public String webtitle; // "",
    //public String sSystemImagesAndCssPath; // "",
    public String customer_service_link; // "https://www.tyughj.bar/service/chatlink.html,https://www.xcsdef.bar/service/chatlink.html",
    //public String pt_download_pc; // "http://cdn.vbet.club/happyslots/d/setupglx.exe",
    //public Object user; // {
    //public String parentid; // "2878927",
    //public String usertype; // "1",
    //public String iscreditaccount; // "0",
    //public String userrank; // "0",
    //public String availablebalance; // "1268.0774",
    //public String preinfo; // "",
    //public String nickname; // "智源",
    //public String messages; // "120"
    //},
    //public String push_service_status; // 1,
    //public Object push_service_module; // {"push_issuetime; //"1","push_issuecode; //"1","push_notice; //"0","push_usermessage; //"1","push_userbalance; //"1","push_userwonprize; //"1"},
    //public String push_server_host; // "hqap3ol.push.com",
    //public String pub_channel_token; // "33c76f70561aa6753c8ff6549d3b296d",
    //public String pub_channel_id; // "3bb42e1dd900147b89ffa2",
    //public String user_channel_id; // "e6c5b80cfab20b88460585",
    //public String topprizes_herolist_enabled; // "1",
    //public String topprizes_publicity_enabled; // "1",
    //public String topprizes_wintips_enabled; // "1",
    //public String quickSearch; // "",
    //public String desK; // "I4vTgcoiz6ZgmOvZbv1EmnjHMcsOYR9L",
    //public Object get; // {
    //public String  p; // "1",
    //public String  userid; // "2888826",
    //public String  username; // null,
    //public String  pn; // "20",
    //public String  startDate; // "2023-08-09 00:00:00",
    //public String  endDate; // "2024-01-09 23:59:59",
    //public String  platform; // "FBXC"
    //}
    public String today; // "2024-01-08",
    public String starttime; // "2023-08-08 00:00:00",
    public String endtime; // "2024-01-09 23:59:59",
    //public HashMap<String, OrderTypeVo> ordertypes; // {

    @SerializedName(value = "mobile_page", alternate = {"pages"})
    @Expose
    public MobilePageVo mobile_page = new MobilePageVo(); // {
    public ICountVo icount; // {
    //    public List<OrderVo> orders; // {
    //public String pageinfo; // "",
    public String username; // "",

    // 失败/不通过,返回错误信息 msg_detail,msg_type,message
    public String msg_detail;
    public int msg_type; // 1 (异常/失败的时候) 1,2-失败, 3-成功
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    public String message; // "页面超时！请重试。",

    public static class ICountVo {
        public String out; // 465,
        public String in; // 465.9,
        public String left; // 0.9
    }

    /**
     * 报表-页信息
     */
    public static class MobilePageVo {
        public int p; // 1,
        public String total_page = "0"; // 84, false
        public int page_size; // 20
    }

}
