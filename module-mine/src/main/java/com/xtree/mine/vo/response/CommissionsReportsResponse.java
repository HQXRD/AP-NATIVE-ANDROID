package com.xtree.mine.vo.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表返回体
 */
public class CommissionsReportsResponse extends BaseResponse2 {

    //{"webtitle":"彩票","sSystemImagesAndCssPath":"","customer_service_link":"https://vscaue.bx64fzsm.com/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8,https://vscaue.tsyan4oo.com/chatwindow.aspx?siteId=60003843&planId=39e9e819-e951-4e32-a173-ee9afd793ab8","pt_download_pc":"http://cdn.vbet.club/happyslots/d/setupglx.exe","user":{"parentid":"0","usertype":"1","iscreditaccount":"0","userrank":"0","availablebalance":"80869.7400","preinfo":"","nickname":"12","messages":"1"},"push_service_status":1,"push_service_module":"{\\\"push_issuetime\\\":\\\"1\\\",\\\"push_issuecode\\\":\\\"1\\\",\\\"push_notice\\\":\\\"0\\\",\\\"push_usermessage\\\":\\\"1\\\",\\\"push_userbalance\\\":\\\"1\\\",\\\"push_userwonprize\\\":\\\"1\\\"}","push_server_host":"apre3push.oxldkm.com","pub_channel_token":"3483e015dd1998213345497a9fe712f7","pub_channel_id":"3bb42e1dd900147b89ffa2","user_channel_id":"983e2f886a7f5e2ff21ee9","topprizes_herolist_enabled":"1","topprizes_publicity_enabled":"1","topprizes_wintips_enabled":"1","desK":"3MYVEEe9cAPqnNJQqZXqOzpQF1qioxHj","s":{"start_time":"2024-02-01","end_time":"2024-02-29"},"commission_info":{"userid":null,"actual":"0.00","adjust":"0.00","status_name":"无","sent_at":"0000-00-00","activity_people":0,"last_remain":"0.00","remain":"0.00","ratio":"0%","income":"0.00","sys_adjust":"0.00","adjust_income":"0.00"},"zhks_info":[]}

    /**
     * sSystemImagesAndCssPath
     */
    @SerializedName("sSystemImagesAndCssPath")
    private String sSystemImagesAndCssPath;
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
     * s
     */
    @SerializedName("s")
    private SDTO s;
    /**
     * commissionInfo
     */
    @SerializedName("commission_info")
    private CommissionInfoDTO commissionInfo;
    /**
     * zhksInfo
     */
    @SerializedName("zhks_info")
    private Object zhksInfo;

    public String getSSystemImagesAndCssPath() {
        return sSystemImagesAndCssPath;
    }

    public void setSSystemImagesAndCssPath(String sSystemImagesAndCssPath) {
        this.sSystemImagesAndCssPath = sSystemImagesAndCssPath;
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

    public SDTO getS() {
        return s;
    }

    public void setS(SDTO s) {
        this.s = s;
    }

    public CommissionInfoDTO getCommissionInfo() {
        return commissionInfo;
    }

    public void setCommissionInfo(CommissionInfoDTO commissionInfo) {
        this.commissionInfo = commissionInfo;
    }

    public ZhksInfoDTO getZhksInfo() {
        Gson gson = new Gson();
        String json = gson.toJson(zhksInfo);
        return gson.fromJson(json, ZhksInfoDTO.class);
    }

    public void setZhksInfo(Object zhksInfo) {
        this.zhksInfo = zhksInfo;
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

    public static class SDTO {
        /**
         * startTime
         */
        @SerializedName("start_time")
        private String startTime;
        /**
         * endTime
         */
        @SerializedName("end_time")
        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public static class CommissionInfoDTO {
        /**
         * userid
         */
        @SerializedName("userid")
        private Object userid;
        /**
         * actual
         */
        @SerializedName("actual")
        private String actual;
        /**
         * adjust
         */
        @SerializedName("adjust")
        private String adjust;
        /**
         * statusName
         */
        @SerializedName("status_name")
        private String statusName;
        /**
         * sentAt
         */
        @SerializedName("sent_at")
        private String sentAt;
        /**
         * activityPeople
         */
        @SerializedName("activity_people")
        private String activityPeople;
        /**
         * lastRemain
         */
        @SerializedName("last_remain")
        private String lastRemain;
        /**
         * remain
         */
        @SerializedName("remain")
        private String remain;
        /**
         * ratio
         */
        @SerializedName("ratio")
        private String ratio;
        /**
         * income
         */
        @SerializedName("income")
        private String income;
        /**
         * sysAdjust
         */
        @SerializedName("sys_adjust")
        private String sysAdjust;
        /**
         * adjustIncome
         */
        @SerializedName("adjust_income")
        private String adjustIncome;

        public Object getUserid() {
            return userid;
        }

        public void setUserid(Object userid) {
            this.userid = userid;
        }

        public String getActual() {
            return actual;
        }

        public void setActual(String actual) {
            this.actual = actual;
        }

        public String getAdjust() {
            return adjust;
        }

        public void setAdjust(String adjust) {
            this.adjust = adjust;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getSentAt() {
            return sentAt;
        }

        public void setSentAt(String sentAt) {
            this.sentAt = sentAt;
        }

        public String getActivityPeople() {
            return activityPeople;
        }

        public void setActivityPeople(String activityPeople) {
            this.activityPeople = activityPeople;
        }

        public String getLastRemain() {
            return lastRemain;
        }

        public void setLastRemain(String lastRemain) {
            this.lastRemain = lastRemain;
        }

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getSysAdjust() {
            return sysAdjust;
        }

        public void setSysAdjust(String sysAdjust) {
            this.sysAdjust = sysAdjust;
        }

        public String getAdjustIncome() {
            return adjustIncome;
        }

        public void setAdjustIncome(String adjustIncome) {
            this.adjustIncome = adjustIncome;
        }
    }

    public static class ZhksInfoDTO {

        /**
         * profitloss
         */
        @SerializedName("profitloss")
        private String profitloss;
        /**
         * ratio
         */
        @SerializedName("ratio")
        private String ratio;
        /**
         * people
         */
        @SerializedName("people")
        private String people;
        /**
         * selfMoney
         */
        @SerializedName("self_money")
        private String selfMoney;

        public String getProfitloss() {
            return profitloss;
        }

        public void setProfitloss(String profitloss) {
            this.profitloss = profitloss;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getPeople() {
            return people;
        }

        public void setPeople(String people) {
            this.people = people;
        }

        public String getSelfMoney() {
            return selfMoney;
        }

        public void setSelfMoney(String selfMoney) {
            this.selfMoney = selfMoney;
        }
    }
}
