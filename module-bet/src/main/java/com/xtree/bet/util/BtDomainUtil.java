package com.xtree.bet.util;

import java.util.ArrayList;
import java.util.List;

public class BtDomainUtil {
    private static List<String> fbDomainUrl = new ArrayList<>();
    private static List<String> fbxcDomainUrl = new ArrayList<>();
    private static String defaultFbDomainUrl;
    private static String defaultFbxcDomainUrl;
    private static String defaultPmDomainUrl;

    public static void addFbDomainUrl(String url){
        BtDomainUtil.fbDomainUrl.clear();
        BtDomainUtil.fbDomainUrl.add(url);
    }

    public static void setFbDomainUrl(List<String> fbDomainUrl) {
        BtDomainUtil.fbDomainUrl.addAll(fbDomainUrl);
    }

    public static List<String> getFbDomainUrl() {
        return fbDomainUrl;
    }

    public static void addFbxcDomainUrl(String url){
        BtDomainUtil.fbxcDomainUrl.clear();
        BtDomainUtil.fbxcDomainUrl.add(url);
    }

    public static void setFbxcDomainUrl(List<String> fbDomainUrl) {
        BtDomainUtil.fbxcDomainUrl.addAll(fbDomainUrl);
    }

    public static List<String> getFbxcDomainUrl() {
        return fbxcDomainUrl;
    }

    public static void setDefaultFbDomainUrl(String defaultFbDomainUrl) {
        BtDomainUtil.defaultFbDomainUrl = defaultFbDomainUrl;
    }

    public static void setDefaultFbxcDomainUrl(String defaultFbxcDomainUrl) {
        BtDomainUtil.defaultFbxcDomainUrl = defaultFbxcDomainUrl;
    }

    public static String getDefaultPmDomainUrl() {
        return defaultPmDomainUrl;
    }

    public static void setDefaultPmDomainUrl(String defaultPmDomainUrl) {
        BtDomainUtil.defaultPmDomainUrl = defaultPmDomainUrl;
    }
}
