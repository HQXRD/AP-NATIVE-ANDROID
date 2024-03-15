package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by KAKA on 2024/3/15.
 * Describe:
 */
public class GameDividendAgrtResponse {

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
    private GetDTO get;
    private MobilePageDTO mobile_page;
    private String billType;
    private String peopleType;
    private HashMap<String,String> billStatus;
    private HashMap<String,CyclesDTO> cycles;
    private SelfBillDTO selfBill;
    private String iUserid;
    private CurrentCycleDTO currentCycle;
    private SelfPayStatusDTO selfPayStatus;
    private ChildrenBillDTO childrenBill;
    private String pageinfo;
    private int selfMax;
    private String setLimit;
    private String setRules;

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

    public GetDTO getGet() {
        return get;
    }

    public void setGet(GetDTO get) {
        this.get = get;
    }

    public MobilePageDTO getMobile_page() {
        return mobile_page;
    }

    public void setMobile_page(MobilePageDTO mobile_page) {
        this.mobile_page = mobile_page;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPeopleType() {
        return peopleType;
    }

    public void setPeopleType(String peopleType) {
        this.peopleType = peopleType;
    }

    public HashMap<String,String> getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(HashMap<String,String> billStatus) {
        this.billStatus = billStatus;
    }

    public HashMap<String,CyclesDTO> getCycles() {
        return cycles;
    }

    public void setCycles(HashMap<String,CyclesDTO> cycles) {
        this.cycles = cycles;
    }

    public SelfBillDTO getSelfBill() {
        return selfBill;
    }

    public void setSelfBill(SelfBillDTO selfBill) {
        this.selfBill = selfBill;
    }

    public String getIUserid() {
        return iUserid;
    }

    public void setIUserid(String iUserid) {
        this.iUserid = iUserid;
    }

    public CurrentCycleDTO getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(CurrentCycleDTO currentCycle) {
        this.currentCycle = currentCycle;
    }

    public SelfPayStatusDTO getSelfPayStatus() {
        return selfPayStatus;
    }

    public void setSelfPayStatus(SelfPayStatusDTO selfPayStatus) {
        this.selfPayStatus = selfPayStatus;
    }

    public ChildrenBillDTO getChildrenBill() {
        return childrenBill;
    }

    public void setChildrenBill(ChildrenBillDTO childrenBill) {
        this.childrenBill = childrenBill;
    }

    public String getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(String pageinfo) {
        this.pageinfo = pageinfo;
    }

    public int getSelfMax() {
        return selfMax;
    }

    public void setSelfMax(int selfMax) {
        this.selfMax = selfMax;
    }

    public String getSetLimit() {
        return setLimit;
    }

    public void setSetLimit(String setLimit) {
        this.setLimit = setLimit;
    }

    public String getSetRules() {
        return setRules;
    }

    public void setSetRules(String setRules) {
        this.setRules = setRules;
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

    public static class GetDTO {
        private String sort;
        private String orderby;
        private String username;
        private int cycle_id;
        private String pay_status;

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getOrderby() {
            return orderby;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getCycle_id() {
            return cycle_id;
        }

        public void setCycle_id(int cycle_id) {
            this.cycle_id = cycle_id;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }
    }

    public static class MobilePageDTO {
        private String p;
        private String total_page;
        private String page_size;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getTotal_page() {
            return total_page;
        }

        public void setTotal_page(String total_page) {
            this.total_page = total_page;
        }

        public String getPage_size() {
            return page_size;
        }

        public void setPage_size(String page_size) {
            this.page_size = page_size;
        }
    }

    public static class SelfBillDTO {
        private int profitloss;

        public int getProfitloss() {
            return profitloss;
        }

        public void setProfitloss(int profitloss) {
            this.profitloss = profitloss;
        }
    }

    public static class CurrentCycleDTO {
        private String id;
        private String title;
        private String type;
        private String cycle_type;
        private String cycle_date;
        private String startDate;
        private String endDate;
        private String createTime;
        private String is_pay;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCycle_type() {
            return cycle_type;
        }

        public void setCycle_type(String cycle_type) {
            this.cycle_type = cycle_type;
        }

        public String getCycle_date() {
            return cycle_date;
        }

        public void setCycle_date(String cycle_date) {
            this.cycle_date = cycle_date;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }
    }

    public static class SelfPayStatusDTO {
        private int payoff;
        private int owe;

        public int getPayoff() {
            return payoff;
        }

        public void setPayoff(int payoff) {
            this.payoff = payoff;
        }

        public int getOwe() {
            return owe;
        }

        public void setOwe(int owe) {
            this.owe = owe;
        }
    }

    public static class ChildrenBillDTO {
        private int total_size;
        private List<DataDTO> data;
        private AllTotalDTO all_total;
        private Object self;
        private List<ChildrenDTO> children;
        private CurTotalDTO cur_total;

        public int getTotal_size() {
            return total_size;
        }

        public void setTotal_size(int total_size) {
            this.total_size = total_size;
        }

        public List<DataDTO> getData() {
            return data;
        }

        public void setData(List<DataDTO> data) {
            this.data = data;
        }

        public AllTotalDTO getAll_total() {
            return all_total;
        }

        public void setAll_total(AllTotalDTO all_total) {
            this.all_total = all_total;
        }

        public Object getSelf() {
            return self;
        }

        public void setSelf(Object self) {
            this.self = self;
        }

        public List<ChildrenDTO> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenDTO> children) {
            this.children = children;
        }

        public CurTotalDTO getCur_total() {
            return cur_total;
        }

        public void setCur_total(CurTotalDTO cur_total) {
            this.cur_total = cur_total;
        }

        public static class AllTotalDTO {
            private int sum_bet;
            private int sum_profitloss;
            private int sum_money;

            public int getSum_bet() {
                return sum_bet;
            }

            public void setSum_bet(int sum_bet) {
                this.sum_bet = sum_bet;
            }

            public int getSum_profitloss() {
                return sum_profitloss;
            }

            public void setSum_profitloss(int sum_profitloss) {
                this.sum_profitloss = sum_profitloss;
            }

            public int getSum_money() {
                return sum_money;
            }

            public void setSum_money(int sum_money) {
                this.sum_money = sum_money;
            }
        }

        public static class CurTotalDTO {
            private int sum_bet;
            private int sum_profitloss;
            private int sum_money;

            public int getSum_bet() {
                return sum_bet;
            }

            public void setSum_bet(int sum_bet) {
                this.sum_bet = sum_bet;
            }

            public int getSum_profitloss() {
                return sum_profitloss;
            }

            public void setSum_profitloss(int sum_profitloss) {
                this.sum_profitloss = sum_profitloss;
            }

            public int getSum_money() {
                return sum_money;
            }

            public void setSum_money(int sum_money) {
                this.sum_money = sum_money;
            }
        }

        public static class DataDTO {
            private String userid;
            private String username;
            private String people;
            private String day_people;
            private String week_people;
            private String bet;
            private String profitloss;
            private String cycle_percent;
            private String sub_money;
            private String self_money;
            private String cycle_id;
            private String pay_status;
            private String is_lvtop;
            private String netloss;
            private String contract_status;
            private String label;

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

            public String getPeople() {
                return people;
            }

            public void setPeople(String people) {
                this.people = people;
            }

            public String getDay_people() {
                return day_people;
            }

            public void setDay_people(String day_people) {
                this.day_people = day_people;
            }

            public String getWeek_people() {
                return week_people;
            }

            public void setWeek_people(String week_people) {
                this.week_people = week_people;
            }

            public String getBet() {
                return bet;
            }

            public void setBet(String bet) {
                this.bet = bet;
            }

            public String getProfitloss() {
                return profitloss;
            }

            public void setProfitloss(String profitloss) {
                this.profitloss = profitloss;
            }

            public String getCycle_percent() {
                return cycle_percent;
            }

            public void setCycle_percent(String cycle_percent) {
                this.cycle_percent = cycle_percent;
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

            public String getCycle_id() {
                return cycle_id;
            }

            public void setCycle_id(String cycle_id) {
                this.cycle_id = cycle_id;
            }

            public String getPay_status() {
                return pay_status;
            }

            public void setPay_status(String pay_status) {
                this.pay_status = pay_status;
            }

            public String getIs_lvtop() {
                return is_lvtop;
            }

            public void setIs_lvtop(String is_lvtop) {
                this.is_lvtop = is_lvtop;
            }

            public String getNetloss() {
                return netloss;
            }

            public void setNetloss(String netloss) {
                this.netloss = netloss;
            }

            public String getContract_status() {
                return contract_status;
            }

            public void setContract_status(String contract_status) {
                this.contract_status = contract_status;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }

        public static class ChildrenDTO {
            private String userid;
            private String username;

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
        }
    }

    public static class CyclesDTO {
        private String id;
        private String title;
        private String type;
        private String cycle_type;
        private String cycle_date;
        private String startDate;
        private String endDate;
        private String createTime;
        private String is_pay;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCycle_type() {
            return cycle_type;
        }

        public void setCycle_type(String cycle_type) {
            this.cycle_type = cycle_type;
        }

        public String getCycle_date() {
            return cycle_date;
        }

        public void setCycle_date(String cycle_date) {
            this.cycle_date = cycle_date;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }
    }
}
