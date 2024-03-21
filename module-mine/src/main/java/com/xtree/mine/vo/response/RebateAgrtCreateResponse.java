package com.xtree.mine.vo.response;

import java.util.List;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class RebateAgrtCreateResponse {

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
    private String sMsg;
    private String msg_detail;
    private String sTarget;
    private String sUrl;
    private List<?> sdata;

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

    public String getSMsg() {
        return sMsg;
    }

    public void setSMsg(String sMsg) {
        this.sMsg = sMsg;
    }

    public String getMsg_detail() {
        return msg_detail;
    }

    public void setMsg_detail(String msg_detail) {
        this.msg_detail = msg_detail;
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
}
