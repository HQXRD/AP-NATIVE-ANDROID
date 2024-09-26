package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/9/26.
 * Describe: 招商月度佣金查看契约返回体
 */
public class CommissionsDividendResponse {

    //{"webtitle":"\u5f69\u7968","sSystemImagesAndCssPath":"","customer_service_link":"https:\/\/vscaue.bx64fzsm.com\/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8,https:\/\/vscaue.tsyan4oo.com\/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8","pt_download_pc":"http:\/\/cdn.vbet.club\/happyslots\/d\/setupglx.exe","user":{"parentid":"2792837","usertype":"1","iscreditaccount":"0","userrank":"0","availablebalance":"9000.0000","preinfo":"","nickname":"sf0001@e3","messages":"5"},"push_service_status":1,"push_service_module":"{\\\"push_issuetime\\\":\\\"1\\\",\\\"push_issuecode\\\":\\\"1\\\",\\\"push_notice\\\":\\\"0\\\",\\\"push_usermessage\\\":\\\"1\\\",\\\"push_userbalance\\\":\\\"1\\\",\\\"push_userwonprize\\\":\\\"1\\\"}","push_server_host":"apre3push.oxldkm.com","pub_channel_token":"3f3037b3ad67ecc5dbfe2d4639641be1","pub_channel_id":"3bb42e1dd900147b89ffa2","user_channel_id":"d8cca6133bb9362b0b979b","topprizes_herolist_enabled":"1","topprizes_publicity_enabled":"1","topprizes_wintips_enabled":"1","desK":"ozR8G12ZdpiD3bercdmtNoEG3lroNW7A","rule":[{"id":"3456","ratio":"30.00","new_activity_people_comparison":"1","new_activity_people":"0","activity_people_comparison":"1","activity_people":"2","real_bet_comparison":"1","real_bet":"0","income_comparison":"1","income":"1","plan_id":"884"},{"id":"3457","ratio":"35.00","new_activity_people_comparison":"1","new_activity_people":"0","activity_people_comparison":"1","activity_people":"3","real_bet_comparison":"1","real_bet":"0","income_comparison":"1","income":"100","plan_id":"884"},{"id":"3458","ratio":"40.00","new_activity_people_comparison":"1","new_activity_people":"0","activity_people_comparison":"1","activity_people":"4","real_bet_comparison":"1","real_bet":"0","income_comparison":"1","income":"200","plan_id":"884"},{"id":"3459","ratio":"45.00","new_activity_people_comparison":"1","new_activity_people":"0","activity_people_comparison":"1","activity_people":"5","real_bet_comparison":"1","real_bet":"0","income_comparison":"1","income":"300","plan_id":"884"},{"id":"3460","ratio":"50.00","new_activity_people_comparison":"1","new_activity_people":"0","activity_people_comparison":"1","activity_people":"6","real_bet_comparison":"1","real_bet":"0","income_comparison":"1","income":"400","plan_id":"884"},{"id":"3461","ratio":"55.00","new_activity_people_comparison":"1","new_activity_people":"0","activity_people_comparison":"1","activity_people":"7","real_bet_comparison":"1","real_bet":"0","income_comparison":"1","income":"500","plan_id":"884"}]}

    /**
     * webtitle
     */
    @SerializedName("webtitle")
    private String webtitle;
    /**
     * sSystemImagesAndCssPath
     */
    @SerializedName("sSystemImagesAndCssPath")
    private String sSystemImagesAndCssPath;
    /**
     * customerServiceLink
     */
    @SerializedName("customer_service_link")
    private String customerServiceLink;
    /**
     * ptDownloadPc
     */
    @SerializedName("pt_download_pc")
    private String ptDownloadPc;
    /**
     * user
     */
    @SerializedName("user")
    private UserDTO user;
    /**
     * pushServiceStatus
     */
    @SerializedName("push_service_status")
    private int pushServiceStatus;
    /**
     * pushServiceModule
     */
    @SerializedName("push_service_module")
    private String pushServiceModule;
    /**
     * pushServerHost
     */
    @SerializedName("push_server_host")
    private String pushServerHost;
    /**
     * pubChannelToken
     */
    @SerializedName("pub_channel_token")
    private String pubChannelToken;
    /**
     * pubChannelId
     */
    @SerializedName("pub_channel_id")
    private String pubChannelId;
    /**
     * userChannelId
     */
    @SerializedName("user_channel_id")
    private String userChannelId;
    /**
     * topprizesHerolistEnabled
     */
    @SerializedName("topprizes_herolist_enabled")
    private String topprizesHerolistEnabled;
    /**
     * topprizesPublicityEnabled
     */
    @SerializedName("topprizes_publicity_enabled")
    private String topprizesPublicityEnabled;
    /**
     * topprizesWintipsEnabled
     */
    @SerializedName("topprizes_wintips_enabled")
    private String topprizesWintipsEnabled;
    /**
     * desK
     */
    @SerializedName("desK")
    private String desK;
    /**
     * rule
     */
    @SerializedName("rule")
    private List<RuleDTO> rule;

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

    public String getCustomerServiceLink() {
        return customerServiceLink;
    }

    public void setCustomerServiceLink(String customerServiceLink) {
        this.customerServiceLink = customerServiceLink;
    }

    public String getPtDownloadPc() {
        return ptDownloadPc;
    }

    public void setPtDownloadPc(String ptDownloadPc) {
        this.ptDownloadPc = ptDownloadPc;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getPushServiceStatus() {
        return pushServiceStatus;
    }

    public void setPushServiceStatus(int pushServiceStatus) {
        this.pushServiceStatus = pushServiceStatus;
    }

    public String getPushServiceModule() {
        return pushServiceModule;
    }

    public void setPushServiceModule(String pushServiceModule) {
        this.pushServiceModule = pushServiceModule;
    }

    public String getPushServerHost() {
        return pushServerHost;
    }

    public void setPushServerHost(String pushServerHost) {
        this.pushServerHost = pushServerHost;
    }

    public String getPubChannelToken() {
        return pubChannelToken;
    }

    public void setPubChannelToken(String pubChannelToken) {
        this.pubChannelToken = pubChannelToken;
    }

    public String getPubChannelId() {
        return pubChannelId;
    }

    public void setPubChannelId(String pubChannelId) {
        this.pubChannelId = pubChannelId;
    }

    public String getUserChannelId() {
        return userChannelId;
    }

    public void setUserChannelId(String userChannelId) {
        this.userChannelId = userChannelId;
    }

    public String getTopprizesHerolistEnabled() {
        return topprizesHerolistEnabled;
    }

    public void setTopprizesHerolistEnabled(String topprizesHerolistEnabled) {
        this.topprizesHerolistEnabled = topprizesHerolistEnabled;
    }

    public String getTopprizesPublicityEnabled() {
        return topprizesPublicityEnabled;
    }

    public void setTopprizesPublicityEnabled(String topprizesPublicityEnabled) {
        this.topprizesPublicityEnabled = topprizesPublicityEnabled;
    }

    public String getTopprizesWintipsEnabled() {
        return topprizesWintipsEnabled;
    }

    public void setTopprizesWintipsEnabled(String topprizesWintipsEnabled) {
        this.topprizesWintipsEnabled = topprizesWintipsEnabled;
    }

    public String getDesK() {
        return desK;
    }

    public void setDesK(String desK) {
        this.desK = desK;
    }

    public List<RuleDTO> getRule() {
        return rule;
    }

    public void setRule(List<RuleDTO> rule) {
        this.rule = rule;
    }

    public static class UserDTO {
        /**
         * parentid
         */
        @SerializedName("parentid")
        private String parentid;
        /**
         * usertype
         */
        @SerializedName("usertype")
        private String usertype;
        /**
         * iscreditaccount
         */
        @SerializedName("iscreditaccount")
        private String iscreditaccount;
        /**
         * userrank
         */
        @SerializedName("userrank")
        private String userrank;
        /**
         * availablebalance
         */
        @SerializedName("availablebalance")
        private String availablebalance;
        /**
         * preinfo
         */
        @SerializedName("preinfo")
        private String preinfo;
        /**
         * nickname
         */
        @SerializedName("nickname")
        private String nickname;
        /**
         * messages
         */
        @SerializedName("messages")
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

    public static class RuleDTO {
        /**
         * id
         */
        @SerializedName("id")
        private String id;
        /**
         * ratio
         */
        @SerializedName("ratio")
        private String ratio;
        /**
         * newActivityPeopleComparison
         */
        @SerializedName("new_activity_people_comparison")
        private String newActivityPeopleComparison;
        /**
         * newActivityPeople
         */
        @SerializedName("new_activity_people")
        private String newActivityPeople;
        /**
         * activityPeopleComparison
         */
        @SerializedName("activity_people_comparison")
        private String activityPeopleComparison;
        /**
         * activityPeople
         */
        @SerializedName("activity_people")
        private String activityPeople;
        /**
         * realBetComparison
         */
        @SerializedName("real_bet_comparison")
        private String realBetComparison;
        /**
         * realBet
         */
        @SerializedName("real_bet")
        private String realBet;
        /**
         * incomeComparison
         */
        @SerializedName("income_comparison")
        private String incomeComparison;
        /**
         * income
         */
        @SerializedName("income")
        private String income;
        /**
         * planId
         */
        @SerializedName("plan_id")
        private String planId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getNewActivityPeopleComparison() {
            return newActivityPeopleComparison;
        }

        public void setNewActivityPeopleComparison(String newActivityPeopleComparison) {
            this.newActivityPeopleComparison = newActivityPeopleComparison;
        }

        public String getNewActivityPeople() {
            return newActivityPeople;
        }

        public void setNewActivityPeople(String newActivityPeople) {
            this.newActivityPeople = newActivityPeople;
        }

        public String getActivityPeopleComparison() {
            return activityPeopleComparison;
        }

        public void setActivityPeopleComparison(String activityPeopleComparison) {
            this.activityPeopleComparison = activityPeopleComparison;
        }

        public String getActivityPeople() {
            return activityPeople;
        }

        public void setActivityPeople(String activityPeople) {
            this.activityPeople = activityPeople;
        }

        public String getRealBetComparison() {
            return realBetComparison;
        }

        public void setRealBetComparison(String realBetComparison) {
            this.realBetComparison = realBetComparison;
        }

        public String getRealBet() {
            return realBet;
        }

        public void setRealBet(String realBet) {
            this.realBet = realBet;
        }

        public String getIncomeComparison() {
            return incomeComparison;
        }

        public void setIncomeComparison(String incomeComparison) {
            this.incomeComparison = incomeComparison;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }
    }
}
