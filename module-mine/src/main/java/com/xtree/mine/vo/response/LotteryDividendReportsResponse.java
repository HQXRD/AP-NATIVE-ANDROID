package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 彩票分红报表返回体
 */
public class LotteryDividendReportsResponse {

    //{"status":10000,"message":"success","data":{"agentList":{"0":"代理","1":"股东","2":"主管","3":"招商","4":"一级代理","5":"第5层","6":"第6层","7":"第7层","8":"第8层","9":"第9层","10":"第10层"},"pages":"总计 81 个记录,  分为 5 页, 当前第 1 页<span id=\"tPages\">   <STRONG>1</STRONG>\n<a  href=\"/api/report/getpink/?cycleid=&username=&agency_model=&status=1&p=2&pn=20\">2</a>\n<a  href=\"/api/report/getpink/?cycleid=&username=&agency_model=&status=1&p=3&pn=20\">3</a>\n<a  href=\"/api/report/getpink/?cycleid=&username=&agency_model=&status=1&p=4&pn=20\">4</a>\n<a  href=\"/api/report/getpink/?cycleid=&username=&agency_model=&status=1&p=5&pn=20\">5</a>\n </span>\n转至 <script language=\"javascript\">function keepKeyNum(obj,evt){var  k=window.event?evt.keyCode:evt.which; if( k==13 ){ goPage(obj.value);return false; }} function goPage(iPage){if(parseInt(iPage) != iPage){alert(\"输入整数的页码\");return false;} if(parseInt(iPage) < 0){alert(\"输入正整数的页码\");return false;} if( !isNaN(parseInt(iPage)) ) { if(!0){ if( iPage > 5 ){alert(\"输入页码超出尾页页码\");return false; }} window.location.href=\"/api/report/getpink/?cycleid=&username=&agency_model=&status=1&pn=20&p=\"+iPage;}}</script><input onKeyPress=\"return keepKeyNum(this,event);\" type=\"text\" id=\"iGotoPage\" NAME=\"iGotoPage\" size=\"6\">页 <input type=\"button\" onclick=\"javascript:goPage( document.getElementById('iGotoPage').value );return false;\" class=\"button\" value=\"GO\">","totalPage":"81","p":"1","s":{"cycleid":0,"starttime":"2024-06-17 00:00:00","endtime":"2024-10-15 00:00:00","username":"","paystatus":0,"subpaystatus":0,"userid":0,"ordername":"","orderby":"","isall":0,"platform_id":0,"user_level":-1,"m_id":-1},"ur_here":"彩票分红契约报表","aList":[{"id":"1008826","lose_streak":"0","cycle_id":"5719","userid":"5230654","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113246","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230654","username":"allan111","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008824","lose_streak":"0","cycle_id":"5719","userid":"5230649","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113244","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230649","username":"xfh002","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008819","lose_streak":"0","cycle_id":"5719","userid":"5230642","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113238","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230642","username":"yoyo012","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008816","lose_streak":"0","cycle_id":"5719","userid":"5230634","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113234","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230634","username":"toto012","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008814","lose_streak":"0","cycle_id":"5719","userid":"5230619","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113228","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230619","username":"testdurant002","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008813","lose_streak":"0","cycle_id":"5719","userid":"5230617","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113226","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230617","username":"zz00021","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008812","lose_streak":"0","cycle_id":"5719","userid":"5230614","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113223","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230614","username":"zz00011","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008811","lose_streak":"0","cycle_id":"5719","userid":"5230612","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113221","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230612","username":"sfceshi4","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008809","lose_streak":"0","cycle_id":"5719","userid":"5230606","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113219","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230606","username":"rich222","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008807","lose_streak":"0","cycle_id":"5719","userid":"5230603","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113216","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230603","username":"testjolie01","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008796","lose_streak":"1","cycle_id":"5719","userid":"5230591","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"790.0000","profitloss":"-339.2000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113203","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230591","username":"asd222","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008792","lose_streak":"0","cycle_id":"5719","userid":"5230585","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113198","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230585","username":"testsander2","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008781","lose_streak":"0","cycle_id":"5719","userid":"5230565","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113185","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230565","username":"testnik123","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008780","lose_streak":"0","cycle_id":"5719","userid":"5230563","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113184","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230563","username":"testc2","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008779","lose_streak":"0","cycle_id":"5719","userid":"5230560","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"4","contract_id":"113183","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230560","username":"testnikk1234","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008778","lose_streak":"0","cycle_id":"5719","userid":"5230559","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"4","contract_id":"113182","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230559","username":"xiugai1","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008777","lose_streak":"0","cycle_id":"5719","userid":"5230556","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"4","contract_id":"113180","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230556","username":"testyan444","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008776","lose_streak":"0","cycle_id":"5719","userid":"5230555","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113179","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230555","username":"testyan333","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008775","lose_streak":"0","cycle_id":"5719","userid":"5230552","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113177","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230552","username":"testalluba189","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"},{"id":"1008774","lose_streak":"0","cycle_id":"5719","userid":"5230551","people":"0 (周期活跃)","day_people":"0","week_people":"0","bet":"0.0000","profitloss":"0.0000","netloss":"0.0000","ratio":0,"self_money":0,"sub_money":0,"settle_accounts":0,"user_level":"3","contract_id":"113176","pay_status":"3","sub_pay_status":"3","is_lvtop":"2","parentid":"0","lvtopid":"5230551","username":"testalluba172","frozentype":"0","mark_contract":"0","is_start":"0","title":"2024-08-01/2024-08-15(半月)"}],"cycles":[{"id":"5782","title":"2024-08-16/2024-08-31(半月)","type":"30","cycle_type":"3","cycle_date":"1","startDate":"2024-08-16","endDate":"2024-08-31","createTime":"2024-08-16 04:00:02","is_pay":"0"},{"id":"5721","title":"2024-08-01/2024-08-31(月)","type":"30","cycle_type":"4","cycle_date":"1","startDate":"2024-08-01","endDate":"2024-08-31","createTime":"2024-08-01 04:30:02","is_pay":"0"},{"id":"5720","title":"2024-07-01/2024-07-31(月)","type":"30","cycle_type":"4","cycle_date":"1","startDate":"2024-07-01","endDate":"2024-07-31","createTime":"2024-08-01 04:30:02","is_pay":"0"},{"id":"5719","title":"2024-08-01/2024-08-15(半月)","type":"30","cycle_type":"3","cycle_date":"1","startDate":"2024-08-01","endDate":"2024-08-15","createTime":"2024-08-01 04:00:02","is_pay":"0"},{"id":"5718","title":"2024-07-16/2024-07-31(半月)","type":"30","cycle_type":"3","cycle_date":"1","startDate":"2024-07-16","endDate":"2024-07-31","createTime":"2024-08-01 04:00:02","is_pay":"0"}],"marklist":[],"cycleTitle":"","pageCount":{"bet":"790.0000","profitloss":"-339.2000","self_money":0,"netloss":"0.0000"},"allCount":{"bet":"12230.8420","profitloss":"+3483.6360","self_money":0,"netloss":0},"type":30},"timestamp":1723786777}

    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataDTO data;
    @SerializedName("timestamp")
    private Integer timestamp;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTO {
        @SerializedName("agentList")
        private HashMap<String, String> agentList;
        @SerializedName("pages")
        private String pages;
        @SerializedName("totalPage")
        private String totalPage;
        @SerializedName("p")
        private String p;
        @SerializedName("s")
        private SDTO s;
        @SerializedName("ur_here")
        private String urHere;
        @SerializedName("cycleTitle")
        private String cycleTitle;
        @SerializedName("pageCount")
        private PageCountDTO pageCount;
        @SerializedName("allCount")
        private AllCountDTO allCount;
        @SerializedName("type")
        private Integer type;
        @SerializedName("aList")
        private List<AListDTO> aList;
        @SerializedName("cycles")
        private List<CyclesDTO> cycles;
        @SerializedName("marklist")
        private List<?> marklist;

        public HashMap<String, String> getAgentList() {
            return agentList;
        }

        public void setAgentList(HashMap<String, String> agentList) {
            this.agentList = agentList;
        }

        public String getPages() {
            return pages;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public SDTO getS() {
            return s;
        }

        public void setS(SDTO s) {
            this.s = s;
        }

        public String getUrHere() {
            return urHere;
        }

        public void setUrHere(String urHere) {
            this.urHere = urHere;
        }

        public String getCycleTitle() {
            return cycleTitle;
        }

        public void setCycleTitle(String cycleTitle) {
            this.cycleTitle = cycleTitle;
        }

        public PageCountDTO getPageCount() {
            return pageCount;
        }

        public void setPageCount(PageCountDTO pageCount) {
            this.pageCount = pageCount;
        }

        public AllCountDTO getAllCount() {
            return allCount;
        }

        public void setAllCount(AllCountDTO allCount) {
            this.allCount = allCount;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public List<AListDTO> getAList() {
            return aList;
        }

        public void setAList(List<AListDTO> aList) {
            this.aList = aList;
        }

        public List<CyclesDTO> getCycles() {
            return cycles;
        }

        public void setCycles(List<CyclesDTO> cycles) {
            this.cycles = cycles;
        }

        public List<?> getMarklist() {
            return marklist;
        }

        public void setMarklist(List<?> marklist) {
            this.marklist = marklist;
        }

        public static class SDTO {
            @SerializedName("cycleid")
            private Integer cycleid;
            @SerializedName("starttime")
            private String starttime;
            @SerializedName("endtime")
            private String endtime;
            @SerializedName("username")
            private String username;
            @SerializedName("paystatus")
            private Integer paystatus;
            @SerializedName("subpaystatus")
            private Integer subpaystatus;
            @SerializedName("userid")
            private Integer userid;
            @SerializedName("ordername")
            private String ordername;
            @SerializedName("orderby")
            private String orderby;
            @SerializedName("isall")
            private Integer isall;
            @SerializedName("platform_id")
            private Integer platformId;
            @SerializedName("user_level")
            private Integer userLevel;
            @SerializedName("m_id")
            private Integer mId;

            public Integer getCycleid() {
                return cycleid;
            }

            public void setCycleid(Integer cycleid) {
                this.cycleid = cycleid;
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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public Integer getPaystatus() {
                return paystatus;
            }

            public void setPaystatus(Integer paystatus) {
                this.paystatus = paystatus;
            }

            public Integer getSubpaystatus() {
                return subpaystatus;
            }

            public void setSubpaystatus(Integer subpaystatus) {
                this.subpaystatus = subpaystatus;
            }

            public Integer getUserid() {
                return userid;
            }

            public void setUserid(Integer userid) {
                this.userid = userid;
            }

            public String getOrdername() {
                return ordername;
            }

            public void setOrdername(String ordername) {
                this.ordername = ordername;
            }

            public String getOrderby() {
                return orderby;
            }

            public void setOrderby(String orderby) {
                this.orderby = orderby;
            }

            public Integer getIsall() {
                return isall;
            }

            public void setIsall(Integer isall) {
                this.isall = isall;
            }

            public Integer getPlatformId() {
                return platformId;
            }

            public void setPlatformId(Integer platformId) {
                this.platformId = platformId;
            }

            public Integer getUserLevel() {
                return userLevel;
            }

            public void setUserLevel(Integer userLevel) {
                this.userLevel = userLevel;
            }

            public Integer getMId() {
                return mId;
            }

            public void setMId(Integer mId) {
                this.mId = mId;
            }
        }

        public static class PageCountDTO {
            @SerializedName("bet")
            private String bet;
            @SerializedName("profitloss")
            private String profitloss;
            @SerializedName("self_money")
            private Integer selfMoney;
            @SerializedName("netloss")
            private String netloss;

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

            public Integer getSelfMoney() {
                return selfMoney;
            }

            public void setSelfMoney(Integer selfMoney) {
                this.selfMoney = selfMoney;
            }

            public String getNetloss() {
                return netloss;
            }

            public void setNetloss(String netloss) {
                this.netloss = netloss;
            }
        }

        public static class AllCountDTO {
            @SerializedName("bet")
            private String bet;
            @SerializedName("profitloss")
            private String profitloss;
            @SerializedName("self_money")
            private String selfMoney;
            @SerializedName("netloss")
            private String netloss;

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

            public String getSelfMoney() {
                return selfMoney;
            }

            public void setSelfMoney(String selfMoney) {
                this.selfMoney = selfMoney;
            }

            public String getNetloss() {
                return netloss;
            }

            public void setNetloss(String netloss) {
                this.netloss = netloss;
            }
        }

        public static class AListDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("lose_streak")
            private String loseStreak;
            @SerializedName("cycle_id")
            private String cycleId;
            @SerializedName("userid")
            private String userid;
            @SerializedName("people")
            private String people;
            @SerializedName("day_people")
            private String dayPeople;
            @SerializedName("week_people")
            private String weekPeople;
            @SerializedName("bet")
            private String bet;
            @SerializedName("profitloss")
            private String profitloss;
            @SerializedName("netloss")
            private String netloss;
            @SerializedName("ratio")
            private String ratio;
            @SerializedName("self_money")
            private String selfMoney;
            @SerializedName("sub_money")
            private String subMoney;
            @SerializedName("settle_accounts")
            private String settleAccounts;
            @SerializedName("user_level")
            private String userLevel;
            @SerializedName("contract_id")
            private String contractId;
            @SerializedName("pay_status")
            private String payStatus;
            @SerializedName("sub_pay_status")
            private String subPayStatus;
            @SerializedName("is_lvtop")
            private String isLvtop;
            @SerializedName("parentid")
            private String parentid;
            @SerializedName("lvtopid")
            private String lvtopid;
            @SerializedName("username")
            private String username;
            @SerializedName("frozentype")
            private String frozentype;
            @SerializedName("mark_contract")
            private String markContract;
            @SerializedName("is_start")
            private String isStart;
            @SerializedName("title")
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLoseStreak() {
                return loseStreak;
            }

            public void setLoseStreak(String loseStreak) {
                this.loseStreak = loseStreak;
            }

            public String getCycleId() {
                return cycleId;
            }

            public void setCycleId(String cycleId) {
                this.cycleId = cycleId;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
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

            public String getSettleAccounts() {
                return settleAccounts;
            }

            public void setSettleAccounts(String settleAccounts) {
                this.settleAccounts = settleAccounts;
            }

            public String getUserLevel() {
                return userLevel;
            }

            public void setUserLevel(String userLevel) {
                this.userLevel = userLevel;
            }

            public String getContractId() {
                return contractId;
            }

            public void setContractId(String contractId) {
                this.contractId = contractId;
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

            public String getIsLvtop() {
                return isLvtop;
            }

            public void setIsLvtop(String isLvtop) {
                this.isLvtop = isLvtop;
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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getFrozentype() {
                return frozentype;
            }

            public void setFrozentype(String frozentype) {
                this.frozentype = frozentype;
            }

            public String getMarkContract() {
                return markContract;
            }

            public void setMarkContract(String markContract) {
                this.markContract = markContract;
            }

            public String getIsStart() {
                return isStart;
            }

            public void setIsStart(String isStart) {
                this.isStart = isStart;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class CyclesDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("type")
            private String type;
            @SerializedName("cycle_type")
            private String cycleType;
            @SerializedName("cycle_date")
            private String cycleDate;
            @SerializedName("startDate")
            private String startDate;
            @SerializedName("endDate")
            private String endDate;
            @SerializedName("createTime")
            private String createTime;
            @SerializedName("is_pay")
            private String isPay;

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

            public String getCycleType() {
                return cycleType;
            }

            public void setCycleType(String cycleType) {
                this.cycleType = cycleType;
            }

            public String getCycleDate() {
                return cycleDate;
            }

            public void setCycleDate(String cycleDate) {
                this.cycleDate = cycleDate;
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

            public String getIsPay() {
                return isPay;
            }

            public void setIsPay(String isPay) {
                this.isPay = isPay;
            }
        }
    }
}
