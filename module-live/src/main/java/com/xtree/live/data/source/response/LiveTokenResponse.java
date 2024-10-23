package com.xtree.live.data.source.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/10/23.
 * Describe:
 */
public class LiveTokenResponse {

    //{"status":10000,"message":"\u767b\u5f55\u6210\u529f","data":{"xLiveToken":"d7beb01fb0937ded02f3ca73afdf332eb8d1af5d13469b66c946106203a2ee996e94fd33bf593e304dabc7fa5e68a221","visitor_id":11,"channel_code":"xt","web_api":["https:\/\/zhibo-apis.oxldkm.com"],"app_api":["https:\/\/zhibo-apps.oxldkm.com"]},"timestamp":1729674155}

    /**
     * xLiveToken
     */
    @SerializedName("xLiveToken")
    private String xLiveToken;
    /**
     * visitorId
     */
    @SerializedName("visitor_id")
    private String visitorId;
    /**
     * channelCode
     */
    @SerializedName("channel_code")
    private String channelCode;
    /**
     * webApi
     */
    @SerializedName("web_api")
    private List<String> webApi;
    /**
     * appApi
     */
    @SerializedName("app_api")
    private List<String> appApi;

    public String getXLiveToken() {
        return xLiveToken;
    }

    public void setXLiveToken(String xLiveToken) {
        this.xLiveToken = xLiveToken;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public List<String> getWebApi() {
        return webApi;
    }

    public void setWebApi(List<String> webApi) {
        this.webApi = webApi;
    }

    public List<String> getAppApi() {
        return appApi;
    }

    public void setAppApi(List<String> appApi) {
        this.appApi = appApi;
    }
}
