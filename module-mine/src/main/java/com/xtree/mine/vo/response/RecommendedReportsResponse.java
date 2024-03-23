package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * Created by KAKA on 2024/3/15.
 * Describe: 推荐报表返回体
 */
public class RecommendedReportsResponse extends BaseResponse2 {

    //{"webtitle":"\u5f69\u7968","sSystemImagesAndCssPath":"","customer_service_link":"https:\/\/vscaue.bx64fzsm.com\/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8,https:\/\/vscaue.tsyan4oo.com\/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8","pt_download_pc":"http:\/\/cdn.vbet.club\/happyslots\/d\/setupglx.exe","user":{"parentid":"0","usertype":"1","iscreditaccount":"0","userrank":"0","availablebalance":"519393.8459","preinfo":"\u6d4b\u8bd5\u4e00\u4e0b","nickname":"ceshi1","messages":"55"},"push_service_status":1,"push_service_module":"{\\\"push_issuetime\\\":\\\"1\\\",\\\"push_issuecode\\\":\\\"1\\\",\\\"push_notice\\\":\\\"0\\\",\\\"push_usermessage\\\":\\\"1\\\",\\\"push_userbalance\\\":\\\"1\\\",\\\"push_userwonprize\\\":\\\"1\\\"}","push_server_host":"apre3push.oxldkm.com","pub_channel_token":"33c76f70561aa6753c8ff6549d3b296d","pub_channel_id":"3bb42e1dd900147b89ffa2","user_channel_id":"faeaa969bf22782373791a","topprizes_herolist_enabled":"1","topprizes_publicity_enabled":"1","topprizes_wintips_enabled":"1","desK":"i5OJtB5ubX5GVFnHEcNv8shyVCE36HZF","markct":[],"get":{"sort":"asc","orderby":"pay_status","username":"","cycle_id":9795,"pay_status":"0"},"mobile_page":{"p":"1","total_page":false,"page_size":"10"},"billType":1,"peopleType":"people","billStatus":{"2":"\u5df2\u7ed3\u6e05","1":"\u672a\u7ed3\u6e05","4":"\u5206\u7ea2\u53d6\u6d88","3":"\u65e0\u5206\u7ea2","-1":"\u65e0\u5951\u7ea6\/\u7ec8\u6b62"},"cycles":{"9795":{"id":"9795","title":"2024-03-16\/2024-03-31(\u534a\u6708)","type":"21","cycle_type":"3","cycle_date":"1","startDate":"2024-03-16","endDate":"2024-03-31","createTime":"2024-03-11 18:27:03","is_pay":"0"},"9706":{"id":"9706","title":"2024-03-01\/2024-03-15(\u534a\u6708)","type":"21","cycle_type":"3","cycle_date":"1","startDate":"2024-03-01","endDate":"2024-03-15","createTime":"2024-02-29 10:31:56","is_pay":"0"},"9585":{"id":"9585","title":"2024-02-16\/2024-02-29(\u534a\u6708)","type":"21","cycle_type":"3","cycle_date":"1","startDate":"2024-02-16","endDate":"2024-02-29","createTime":"2024-02-16 05:00:05","is_pay":"1"},"9584":{"id":"9584","title":"2024-02-01\/2024-02-15(\u534a\u6708)","type":"21","cycle_type":"3","cycle_date":"1","startDate":"2024-02-01","endDate":"2024-02-15","createTime":"2024-02-16 05:00:05","is_pay":"0"}},"selfBill":{"profitloss":0},"iUserid":"2792107","currentCycle":{"id":"9795","title":"2024-03-16\/2024-03-31(\u534a\u6708)","type":"21","cycle_type":"3","cycle_date":"1","startDate":"2024-03-16","endDate":"2024-03-31","createTime":"2024-03-11 18:27:03","is_pay":"0"},"selfPayStatus":{"payoff":0,"owe":0},"childrenBill":{"total_size":0,"data":[],"all_total":null,"self":null,"children":[]},"pageinfo":"\u603b\u8ba1 0 \u4e2a\u8bb0\u5f55,  \u5206\u4e3a  \u9875, \u5f53\u524d\u7b2c 1 \u9875\u003Cspan id=\"tPages\"\u003E    \u003Ca  href=\"\/pink\/index\/?userid=&type=21&orderby=pay_status&sort=asc&pay_status=0&username=&cycle_id=&client=m&p=&pn=10\"\u003E\u5c3e\u9875\u003C\/a\u003E\u003C\/span\u003E\n\u8f6c\u81f3 \u003Cscript language=\"javascript\"\u003Efunction keepKeyNum(obj,evt){var  k=window.event?evt.keyCode:evt.which; if( k==13 ){ goPage(obj.value);return false; }} function goPage(iPage){if(parseInt(iPage) != iPage){alert(\"\u8f93\u5165\u6574\u6570\u7684\u9875\u7801\");return false;} if(parseInt(iPage) \u003C 0){alert(\"\u8f93\u5165\u6b63\u6574\u6570\u7684\u9875\u7801\");return false;} if( !isNaN(parseInt(iPage)) ) { if(!0){ if( iPage \u003E 0 ){alert(\"\u8f93\u5165\u9875\u7801\u8d85\u51fa\u5c3e\u9875\u9875\u7801\");return false; }} window.location.href=\"\/pink\/index\/?userid=&type=21&orderby=pay_status&sort=asc&pay_status=0&username=&cycle_id=&client=m&pn=10&p=\"+iPage;}}\u003C\/script\u003E\u003Cinput onKeyPress=\"return keepKeyNum(this,event);\" type=\"text\" id=\"iGotoPage\" NAME=\"iGotoPage\" size=\"6\"\u003E\u9875 \u003Cinput type=\"button\" onclick=\"javascript:goPage( document.getElementById('iGotoPage').value );return false;\" class=\"button\" value=\"GO\"\u003E","selfMax":0,"setLimit":null,"setRules":"[]"}

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
    private String desK;
    private List<?> markct;
    private GetDTO get;
    private int billType;
    private String peopleType;
    private HashMap<String, String> billStatus;
    private HashMap<String, CyclesDTO> cycles;
    private String iUserid;
    private SelfPayStatusDTO selfPayStatus;
    private ChildrenBillDTO childrenBill;
    private String pageinfo;
    private int selfMax;
    private Object setLimit;
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

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public String getPeopleType() {
        return peopleType;
    }

    public void setPeopleType(String peopleType) {
        this.peopleType = peopleType;
    }

    public HashMap<String, String> getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(HashMap<String, String> billStatus) {
        this.billStatus = billStatus;
    }

    public HashMap<String, CyclesDTO> getCycles() {
        return cycles;
    }

    public void setCycles(HashMap<String, CyclesDTO> cycles) {
        this.cycles = cycles;
    }

    public String getIUserid() {
        return iUserid;
    }

    public void setIUserid(String iUserid) {
        this.iUserid = iUserid;
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

    public Object getSetLimit() {
        return setLimit;
    }

    public void setSetLimit(Object setLimit) {
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
        private String cycle_id;
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
        private Object all_total;
        private Object self;
        private List<?> children;
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

        public Object getAll_total() {
            return all_total;
        }

        public void setAll_total(Object all_total) {
            this.all_total = all_total;
        }

        public Object getSelf() {
            return self;
        }

        public void setSelf(Object self) {
            this.self = self;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }

        public CurTotalDTO getCur_total() {
            return cur_total;
        }

        public void setCur_total(CurTotalDTO cur_total) {
            this.cur_total = cur_total;
        }

        public static class CurTotalDTO {
            private int sum_bet;
            private String sum_profitloss;
            private int sum_money;

            public int getSum_bet() {
                return sum_bet;
            }

            public void setSum_bet(int sum_bet) {
                this.sum_bet = sum_bet;
            }

            public String getSum_profitloss() {
                return sum_profitloss;
            }

            public void setSum_profitloss(String sum_profitloss) {
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

            /**
             * id
             */
            @SerializedName("id")
            private String id;
            /**
             * cycleId
             */
            @SerializedName("cycle_id")
            private String cycleId;
            /**
             * contractId
             */
            @SerializedName("contract_id")
            private String contractId;
            /**
             * contractInfoId
             */
            @SerializedName("contract_info_id")
            private String contractInfoId;
            /**
             * userid
             */
            @SerializedName("userid")
            private String userid;
            /**
             * username
             */
            @SerializedName("username")
            private String username;
            /**
             * parentid
             */
            @SerializedName("parentid")
            private String parentid;
            /**
             * cyclePercent
             */
            @SerializedName("cycle_percent")
            private String cyclePercent;
            /**
             * tmpTree
             */
            @SerializedName("tmp_tree")
            private String tmpTree;
            /**
             * lvtopid
             */
            @SerializedName("lvtopid")
            private String lvtopid;
            /**
             * isLvtop
             */
            @SerializedName("is_lvtop")
            private String isLvtop;
            /**
             * people
             */
            @SerializedName("people")
            private String people;
            /**
             * dayPeople
             */
            @SerializedName("day_people")
            private String dayPeople;
            /**
             * weekPeople
             */
            @SerializedName("week_people")
            private String weekPeople;
            /**
             * bet
             */
            @SerializedName("bet")
            private String bet;
            /**
             * lastRemain
             */
            @SerializedName("last_remain")
            private String lastRemain;
            /**
             * profitloss
             */
            @SerializedName("profitloss")
            private String profitloss;
            /**
             * netloss
             */
            @SerializedName("netloss")
            private String netloss;
            /**
             * ratio
             */
            @SerializedName("ratio")
            private String ratio;
            /**
             * selfMoney
             */
            @SerializedName("self_money")
            private String selfMoney;
            /**
             * subMoney
             */
            @SerializedName("sub_money")
            private String subMoney;
            /**
             * payoff
             */
            @SerializedName("payoff")
            private String payoff;
            /**
             * settleAccounts
             */
            @SerializedName("settle_accounts")
            private String settleAccounts;
            /**
             * payStatus
             */
            @SerializedName("pay_status")
            private String payStatus;
            /**
             * subPayStatus
             */
            @SerializedName("sub_pay_status")
            private String subPayStatus;
            /**
             * payTime
             */
            @SerializedName("pay_time")
            private String payTime;
            /**
             * createTime
             */
            @SerializedName("create_time")
            private String createTime;
            /**
             * beforeFrozentype
             */
            @SerializedName("before_frozentype")
            private String beforeFrozentype;
            /**
             * isStart
             */
            @SerializedName("is_start")
            private String isStart;
            /**
             * type
             */
            @SerializedName("type")
            private String type;
            /**
             * distributionType
             */
            @SerializedName("distribution_type")
            private String distributionType;
            /**
             * cycleCliTime
             */
            @SerializedName("cycle_cli_time")
            private String cycleCliTime;
            /**
             * label
             */
            @SerializedName("label")
            private String label;
            /**
             * refUserid
             */
            @SerializedName("ref_userid")
            private String refUserid;
            /**
             * refUsername
             */
            @SerializedName("ref_username")
            private String refUsername;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCycleId() {
                return cycleId;
            }

            public void setCycleId(String cycleId) {
                this.cycleId = cycleId;
            }

            public String getContractId() {
                return contractId;
            }

            public void setContractId(String contractId) {
                this.contractId = contractId;
            }

            public String getContractInfoId() {
                return contractInfoId;
            }

            public void setContractInfoId(String contractInfoId) {
                this.contractInfoId = contractInfoId;
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

            public String getCyclePercent() {
                return cyclePercent;
            }

            public void setCyclePercent(String cyclePercent) {
                this.cyclePercent = cyclePercent;
            }

            public String getTmpTree() {
                return tmpTree;
            }

            public void setTmpTree(String tmpTree) {
                this.tmpTree = tmpTree;
            }

            public String getLvtopid() {
                return lvtopid;
            }

            public void setLvtopid(String lvtopid) {
                this.lvtopid = lvtopid;
            }

            public String getIsLvtop() {
                return isLvtop;
            }

            public void setIsLvtop(String isLvtop) {
                this.isLvtop = isLvtop;
            }

            public String getPeople() {
                return people;
            }

            public void setPeople(String people) {
                this.people = people;
            }

            public String getDayPeople() {
                return dayPeople;
            }

            public void setDayPeople(String dayPeople) {
                this.dayPeople = dayPeople;
            }

            public String getWeekPeople() {
                return weekPeople;
            }

            public void setWeekPeople(String weekPeople) {
                this.weekPeople = weekPeople;
            }

            public String getBet() {
                return bet;
            }

            public void setBet(String bet) {
                this.bet = bet;
            }

            public String getLastRemain() {
                return lastRemain;
            }

            public void setLastRemain(String lastRemain) {
                this.lastRemain = lastRemain;
            }

            public String getProfitloss() {
                return profitloss;
            }

            public void setProfitloss(String profitloss) {
                this.profitloss = profitloss;
            }

            public String getNetloss() {
                return netloss;
            }

            public void setNetloss(String netloss) {
                this.netloss = netloss;
            }

            public String getRatio() {
                return ratio;
            }

            public void setRatio(String ratio) {
                this.ratio = ratio;
            }

            public String getSelfMoney() {
                return selfMoney;
            }

            public void setSelfMoney(String selfMoney) {
                this.selfMoney = selfMoney;
            }

            public String getSubMoney() {
                return subMoney;
            }

            public void setSubMoney(String subMoney) {
                this.subMoney = subMoney;
            }

            public String getPayoff() {
                return payoff;
            }

            public void setPayoff(String payoff) {
                this.payoff = payoff;
            }

            public String getSettleAccounts() {
                return settleAccounts;
            }

            public void setSettleAccounts(String settleAccounts) {
                this.settleAccounts = settleAccounts;
            }

            public String getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(String payStatus) {
                this.payStatus = payStatus;
            }

            public String getSubPayStatus() {
                return subPayStatus;
            }

            public void setSubPayStatus(String subPayStatus) {
                this.subPayStatus = subPayStatus;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getBeforeFrozentype() {
                return beforeFrozentype;
            }

            public void setBeforeFrozentype(String beforeFrozentype) {
                this.beforeFrozentype = beforeFrozentype;
            }

            public String getIsStart() {
                return isStart;
            }

            public void setIsStart(String isStart) {
                this.isStart = isStart;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDistributionType() {
                return distributionType;
            }

            public void setDistributionType(String distributionType) {
                this.distributionType = distributionType;
            }

            public String getCycleCliTime() {
                return cycleCliTime;
            }

            public void setCycleCliTime(String cycleCliTime) {
                this.cycleCliTime = cycleCliTime;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getRefUserid() {
                return refUserid;
            }

            public void setRefUserid(String refUserid) {
                this.refUserid = refUserid;
            }

            public String getRefUsername() {
                return refUsername;
            }

            public void setRefUsername(String refUsername) {
                this.refUsername = refUsername;
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
