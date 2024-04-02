package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: 返水契约游戏场馆返回体
 */
public class GameRebateAgrtResponse extends BaseResponse2 {

    private String sSystemImagesAndCssPath;
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
    private String quickSearch;
    private String desK;
    private Object yesterday_bill;
    private String pageinfo;
    private int isget;
    private TotalDTO total;
    private List<DataDTO> data;
    private boolean description;
    private List<String> chineseNum;
    private GetDTO get;
    private ContractDTO contract;

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

    public String getQuickSearch() {
        return quickSearch;
    }

    public void setQuickSearch(String quickSearch) {
        this.quickSearch = quickSearch;
    }

    public String getDesK() {
        return desK;
    }

    public void setDesK(String desK) {
        this.desK = desK;
    }

    public Object getYesterday_bill() {
        return yesterday_bill;
    }

    public void setYesterday_bill(Object yesterday_bill) {
        this.yesterday_bill = yesterday_bill;
    }

    public String getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(String pageinfo) {
        this.pageinfo = pageinfo;
    }

    public int getIsget() {
        return isget;
    }

    public void setIsget(int isget) {
        this.isget = isget;
    }

    public TotalDTO getTotal() {
        return total;
    }

    public void setTotal(TotalDTO total) {
        this.total = total;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public boolean isDescription() {
        return description;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public List<String> getChineseNum() {
        return chineseNum;
    }

    public void setChineseNum(List<String> chineseNum) {
        this.chineseNum = chineseNum;
    }

    public GetDTO getGet() {
        return get;
    }

    public void setGet(GetDTO get) {
        this.get = get;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
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
        private String total_page;
        private int page_size;

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }

        public String getTotal_page() {
            return total_page;
        }

        public void setTotal_page(String total_page) {
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
        private String sum_liushui;

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

        public String getSum_liushui() {
            return sum_liushui;
        }

        public void setSum_liushui(String sum_liushui) {
            this.sum_liushui = sum_liushui;
        }
    }

    public static class GetDTO {
        private String p;
        private String pstatus;
        private String client;
        private String endtime;
        private String orderby;
        private String sort;
        private String starttime;
        private String pn;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getPstatus() {
            return pstatus;
        }

        public void setPstatus(String pstatus) {
            this.pstatus = pstatus;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

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

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }
    }

    public static class ContractDTO {
        private String id;
        private String type;
        private String userid;
        private String username;
        private String is_lvtop;
        private String user_level;
        private String member_type;
        private String parentid;
        private String lvtopid;
        private String status;
        private String token;
        private String frequency;
        private String effect_date;
        private String sign_time;
        private String create_time;
        private String count_type;
        private List<RuleDTO> rule;

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

        public String getMember_type() {
            return member_type;
        }

        public void setMember_type(String member_type) {
            this.member_type = member_type;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getLvtopid() {
            return lvtopid;
        }

        public void setLvtopid(String lvtopid) {
            this.lvtopid = lvtopid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getEffect_date() {
            return effect_date;
        }

        public void setEffect_date(String effect_date) {
            this.effect_date = effect_date;
        }

        public String getSign_time() {
            return sign_time;
        }

        public void setSign_time(String sign_time) {
            this.sign_time = sign_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCount_type() {
            return count_type;
        }

        public void setCount_type(String count_type) {
            this.count_type = count_type;
        }

        public List<RuleDTO> getRule() {
            return rule;
        }

        public void setRule(List<RuleDTO> rule) {
            this.rule = rule;
        }

        public static class RuleDTO {
            private String level;
            private String min_bet;
            private String min_player;
            private String ratio;
            private String type;
            private String period;
            private String min_netwin;
            private String islock;
            /**
             * bet
             */
            @SerializedName("bet")
            private String bet;
            /**
             * lossAmount
             */
            @SerializedName("loss_amount")
            private String lossAmount;
            /**
             * rtype
             */
            @SerializedName("rtype")
            private String rtype;
            /**
             * people
             */
            @SerializedName("people")
            private String people;


            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getMin_bet() {
                return min_bet;
            }

            public void setMin_bet(String min_bet) {
                this.min_bet = min_bet;
            }

            public String getMin_player() {
                return min_player;
            }

            public void setMin_player(String min_player) {
                this.min_player = min_player;
            }

            public String getRatio() {
                return ratio;
            }

            public void setRatio(String ratio) {
                this.ratio = ratio;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }

            public String getMin_netwin() {
                return min_netwin;
            }

            public void setMin_netwin(String min_netwin) {
                this.min_netwin = min_netwin;
            }

            public String getIslock() {
                return islock;
            }

            public void setIslock(String islock) {
                this.islock = islock;
            }

            public String getBet() {
                return bet;
            }

            public void setBet(String bet) {
                this.bet = bet;
            }

            public String getLossAmount() {
                return lossAmount;
            }

            public void setLossAmount(String lossAmount) {
                this.lossAmount = lossAmount;
            }

            public String getRtype() {
                return rtype;
            }

            public void setRtype(String rtype) {
                this.rtype = rtype;
            }

            public String getPeople() {
                return people;
            }

            public void setPeople(String people) {
                this.people = people;
            }
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
        private int self_money;
        private String pay_money;
        private String pstatus;
        private Object pay_time;
        private String create_time;
        private String platform_id;
        private String liushui;

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

        public int getSelf_money() {
            return self_money;
        }

        public void setSelf_money(int self_money) {
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

        public String getLiushui() {
            return liushui;
        }

        public void setLiushui(String liushui) {
            this.liushui = liushui;
        }
    }
}
