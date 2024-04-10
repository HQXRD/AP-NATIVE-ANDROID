package com.xtree.mine.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/4/6.
 * Describe: 彩票契约创建返回体
 */
public class DividendAgrtCreateResponse {
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
     * sMsg
     */
    @SerializedName("sMsg")
    private String sMsg;
    /**
     * sTarget
     */
    @SerializedName("sTarget")
    private String sTarget;
    /**
     * sUrl
     */
    @SerializedName("sUrl")
    private String sUrl;
    /**
     * sdata
     */
    @SerializedName("sdata")
    private List<?> sdata;
    /**
     * msgDetail
     */
    @SerializedName("msg_detail")
    private String msgDetail;
    /**
     * msgType
     */
    @SerializedName("msg_type")
    private String msgType;
    /**
     * error
     */
    @SerializedName("error")
    private String error;


    public String getsSystemImagesAndCssPath() {
        return sSystemImagesAndCssPath;
    }

    public void setsSystemImagesAndCssPath(String sSystemImagesAndCssPath) {
        this.sSystemImagesAndCssPath = sSystemImagesAndCssPath;
    }

    public String getsMsg() {
        return sMsg;
    }

    public void setsMsg(String sMsg) {
        this.sMsg = sMsg;
    }

    public String getsTarget() {
        return sTarget;
    }

    public void setsTarget(String sTarget) {
        this.sTarget = sTarget;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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

    public String getSMsg() {
        return sMsg;
    }

    public void setSMsg(String sMsg) {
        this.sMsg = sMsg;
    }

    public String getSTarget() {
        return sTarget;
    }

    public void setSTarget(String sTarget) {
        this.sTarget = sTarget;
    }

    public String getSUrl() {
        return sUrl;
    }

    public void setSUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public List<?> getSdata() {
        return sdata;
    }

    public void setSdata(List<?> sdata) {
        this.sdata = sdata;
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

    //{"webtitle":"\u5f69\u7968","sSystemImagesAndCssPath":"","customer_service_link":"https:\/\/sdoiuewa.4r1yq9ul.com\/chatwindow.aspx?siteId=65001210&planId=d40920a9-be7b-49de-9bb3-c9d1c8509ada&chatgroup=3,https:\/\/psowoexvd.1lzq7zyjyce.com\/chatwindow.aspx?siteId=65001210&planId=4e41cf57-c8c0-4899-8466-1b3a2c1b414e&chatgroup=4","pt_download_pc":"http:\/\/cdn.vbet.club\/happyslots\/d\/setupglx.exe","user":{"parentid":"5219996","usertype":"1","iscreditaccount":"0","userrank":"0","availablebalance":"0.0000","preinfo":"","nickname":"zhishu001@e2","messages":"0"},"push_service_status":1,"push_service_module":"{\\\"push_issuetime\\\":\\\"1\\\",\\\"push_issuecode\\\":\\\"1\\\",\\\"push_notice\\\":\\\"0\\\",\\\"push_usermessage\\\":\\\"1\\\",\\\"push_userbalance\\\":\\\"1\\\",\\\"push_userwonprize\\\":\\\"1\\\"}","push_server_host":"eonsport.push.com","pub_channel_token":"3483e015dd1998213345497a9fe712f7","pub_channel_id":"3bb42e1dd900147b89ffa2","user_channel_id":"1301e5ee5ffa3ace39666e","topprizes_herolist_enabled":"1","topprizes_publicity_enabled":"1","topprizes_wintips_enabled":"1","desK":"XWvsuuyHN4pjf2Pe9WoQOcL11ddEcxni","sMsg":"\u7528\u6237\u5951\u7ea6\u521b\u5efa\u6210\u529f\uff01","sTarget":"self","sUrl":"javascript:checkbackspace()","sdata":[]}


}