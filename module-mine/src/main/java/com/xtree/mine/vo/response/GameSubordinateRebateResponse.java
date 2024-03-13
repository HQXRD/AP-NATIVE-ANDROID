package com.xtree.mine.vo.response;

import java.util.List;

/**
 * Created by KAKA on 2024/3/13.
 * Describe:
 */
public class GameSubordinateRebateResponse {

    private String webtitle;
    private String sSystemImagesAndCssPath;
    private String customer_service_link;
    private String pt_download_pc;
    private UserDTO user;
    private int push_service_status;
    private String push_service_module;
    private String push_server_host;
    private String pub_channel_token;
    private String pub_channel_id;
    private String user_channel_id;
    private String topprizes_herolist_enabled;
    private String topprizes_publicity_enabled;
    private String topprizes_wintips_enabled;
    private String desK;
    private List<?> markct;
    private MobilePageDTO mobile_page;
    private int isget;
    private String pageinfo;
    private TotalDTO total;
    private CountDTO count;
    private List<DataDTO> data;
    private GetDTO get;

    public String getWebtitle() {
        return webtitle;
    }

    public void setWebtitle(String webtitle) {
        this.webtitle = webtitle;
    }

    public String getSSystemImagesAndCssPath() {
        return sSystemImagesAndCssPath;
    }

    public void setSSystemImagesAndCssPath(String sSystemImagesAndCssPath) {
        this.sSystemImagesAndCssPath = sSystemImagesAndCssPath;
    }

    public String getCustomer_service_link() {
        return customer_service_link;
    }

    public void setCustomer_service_link(String customer_service_link) {
        this.customer_service_link = customer_service_link;
    }

    public String getPt_download_pc() {
        return pt_download_pc;
    }

    public void setPt_download_pc(String pt_download_pc) {
        this.pt_download_pc = pt_download_pc;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getPush_service_status() {
        return push_service_status;
    }

    public void setPush_service_status(int push_service_status) {
        this.push_service_status = push_service_status;
    }

    public String getPush_service_module() {
        return push_service_module;
    }

    public void setPush_service_module(String push_service_module) {
        this.push_service_module = push_service_module;
    }

    public String getPush_server_host() {
        return push_server_host;
    }

    public void setPush_server_host(String push_server_host) {
        this.push_server_host = push_server_host;
    }

    public String getPub_channel_token() {
        return pub_channel_token;
    }

    public void setPub_channel_token(String pub_channel_token) {
        this.pub_channel_token = pub_channel_token;
    }

    public String getPub_channel_id() {
        return pub_channel_id;
    }

    public void setPub_channel_id(String pub_channel_id) {
        this.pub_channel_id = pub_channel_id;
    }

    public String getUser_channel_id() {
        return user_channel_id;
    }

    public void setUser_channel_id(String user_channel_id) {
        this.user_channel_id = user_channel_id;
    }

    public String getTopprizes_herolist_enabled() {
        return topprizes_herolist_enabled;
    }

    public void setTopprizes_herolist_enabled(String topprizes_herolist_enabled) {
        this.topprizes_herolist_enabled = topprizes_herolist_enabled;
    }

    public String getTopprizes_publicity_enabled() {
        return topprizes_publicity_enabled;
    }

    public void setTopprizes_publicity_enabled(String topprizes_publicity_enabled) {
        this.topprizes_publicity_enabled = topprizes_publicity_enabled;
    }

    public String getTopprizes_wintips_enabled() {
        return topprizes_wintips_enabled;
    }

    public void setTopprizes_wintips_enabled(String topprizes_wintips_enabled) {
        this.topprizes_wintips_enabled = topprizes_wintips_enabled;
    }

    public String getDesK() {
        return desK;
    }

    public void setDesK(String desK) {
        this.desK = desK;
    }

    public List<?> getMarkct() {
        return markct;
    }

    public void setMarkct(List<?> markct) {
        this.markct = markct;
    }

    public MobilePageDTO getMobile_page() {
        return mobile_page;
    }

    public void setMobile_page(MobilePageDTO mobile_page) {
        this.mobile_page = mobile_page;
    }

    public int getIsget() {
        return isget;
    }

    public void setIsget(int isget) {
        this.isget = isget;
    }

    public String getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(String pageinfo) {
        this.pageinfo = pageinfo;
    }

    public TotalDTO getTotal() {
        return total;
    }

    public void setTotal(TotalDTO total) {
        this.total = total;
    }

    public CountDTO getCount() {
        return count;
    }

    public void setCount(CountDTO count) {
        this.count = count;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public GetDTO getGet() {
        return get;
    }

    public void setGet(GetDTO get) {
        this.get = get;
    }

    public static class UserDTO {
        private String parentid;
        private String usertype;
        private String iscreditaccount;
        private String userrank;
        private String availablebalance;
        private String preinfo;
        private String nickname;
        private String messages;

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getIscreditaccount() {
            return iscreditaccount;
        }

        public void setIscreditaccount(String iscreditaccount) {
            this.iscreditaccount = iscreditaccount;
        }

        public String getUserrank() {
            return userrank;
        }

        public void setUserrank(String userrank) {
            this.userrank = userrank;
        }

        public String getAvailablebalance() {
            return availablebalance;
        }

        public void setAvailablebalance(String availablebalance) {
            this.availablebalance = availablebalance;
        }

        public String getPreinfo() {
            return preinfo;
        }

        public void setPreinfo(String preinfo) {
            this.preinfo = preinfo;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMessages() {
            return messages;
        }

        public void setMessages(String messages) {
            this.messages = messages;
        }
    }

    public static class MobilePageDTO {
        private int p;
        private int total_page;
        private int page_size;

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }
    }

    public static class TotalDTO {
        private String sum_bet;
        private String sum_effective_bet;
        private String sum_total_money;
        private String sum_sub_money;
        private String sum_self_money;

        public String getSum_bet() {
            return sum_bet;
        }

        public void setSum_bet(String sum_bet) {
            this.sum_bet = sum_bet;
        }

        public String getSum_effective_bet() {
            return sum_effective_bet;
        }

        public void setSum_effective_bet(String sum_effective_bet) {
            this.sum_effective_bet = sum_effective_bet;
        }

        public String getSum_total_money() {
            return sum_total_money;
        }

        public void setSum_total_money(String sum_total_money) {
            this.sum_total_money = sum_total_money;
        }

        public String getSum_sub_money() {
            return sum_sub_money;
        }

        public void setSum_sub_money(String sum_sub_money) {
            this.sum_sub_money = sum_sub_money;
        }

        public String getSum_self_money() {
            return sum_self_money;
        }

        public void setSum_self_money(String sum_self_money) {
            this.sum_self_money = sum_self_money;
        }
    }

    public static class CountDTO {
        private int bet;
        private int effective_bet;
        private int total_money;
        private int sub_money;
        private int self_money;

        public int getBet() {
            return bet;
        }

        public void setBet(int bet) {
            this.bet = bet;
        }

        public int getEffective_bet() {
            return effective_bet;
        }

        public void setEffective_bet(int effective_bet) {
            this.effective_bet = effective_bet;
        }

        public int getTotal_money() {
            return total_money;
        }

        public void setTotal_money(int total_money) {
            this.total_money = total_money;
        }

        public int getSub_money() {
            return sub_money;
        }

        public void setSub_money(int sub_money) {
            this.sub_money = sub_money;
        }

        public int getSelf_money() {
            return self_money;
        }

        public void setSelf_money(int self_money) {
            this.self_money = self_money;
        }
    }

    public static class GetDTO {
        private String orderby;
        private String sort;
        private String username;
        private String userid;
        private String starttime;
        private String endtime;
        private String pstatus;
        private String p;
        private String pn;
        private String client;

        public String getOrderby() {
            return orderby;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getPstatus() {
            return pstatus;
        }

        public void setPstatus(String pstatus) {
            this.pstatus = pstatus;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
    }

    public static class DataDTO {
        private String id;
        private String type;
        private String contract_id;
        private String contract_info_id;
        private String userid;
        private String username;
        private String parentid;
        private String tmp_tree;
        private String lvtopid;
        private String is_lvtop;
        private String user_level;
        private String bet;
        private String effective_bet;
        private String player_num;
        private String ratio;
        private String date;
        private String total_money;
        private String sub_money;
        private String self_money;
        private String pay_money;
        private String pstatus;
        private Object pay_time;
        private String create_time;
        private String platform_id;
        private Object liushui;
        private String create_date;
        private String label;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContract_id() {
            return contract_id;
        }

        public void setContract_id(String contract_id) {
            this.contract_id = contract_id;
        }

        public String getContract_info_id() {
            return contract_info_id;
        }

        public void setContract_info_id(String contract_info_id) {
            this.contract_info_id = contract_info_id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getTmp_tree() {
            return tmp_tree;
        }

        public void setTmp_tree(String tmp_tree) {
            this.tmp_tree = tmp_tree;
        }

        public String getLvtopid() {
            return lvtopid;
        }

        public void setLvtopid(String lvtopid) {
            this.lvtopid = lvtopid;
        }

        public String getIs_lvtop() {
            return is_lvtop;
        }

        public void setIs_lvtop(String is_lvtop) {
            this.is_lvtop = is_lvtop;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getBet() {
            return bet;
        }

        public void setBet(String bet) {
            this.bet = bet;
        }

        public String getEffective_bet() {
            return effective_bet;
        }

        public void setEffective_bet(String effective_bet) {
            this.effective_bet = effective_bet;
        }

        public String getPlayer_num() {
            return player_num;
        }

        public void setPlayer_num(String player_num) {
            this.player_num = player_num;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
        }

        public String getSub_money() {
            return sub_money;
        }

        public void setSub_money(String sub_money) {
            this.sub_money = sub_money;
        }

        public String getSelf_money() {
            return self_money;
        }

        public void setSelf_money(String self_money) {
            this.self_money = self_money;
        }

        public String getPay_money() {
            return pay_money;
        }

        public void setPay_money(String pay_money) {
            this.pay_money = pay_money;
        }

        public String getPstatus() {
            return pstatus;
        }

        public void setPstatus(String pstatus) {
            this.pstatus = pstatus;
        }

        public Object getPay_time() {
            return pay_time;
        }

        public void setPay_time(Object pay_time) {
            this.pay_time = pay_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPlatform_id() {
            return platform_id;
        }

        public void setPlatform_id(String platform_id) {
            this.platform_id = platform_id;
        }

        public Object getLiushui() {
            return liushui;
        }

        public void setLiushui(Object liushui) {
            this.liushui = liushui;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
