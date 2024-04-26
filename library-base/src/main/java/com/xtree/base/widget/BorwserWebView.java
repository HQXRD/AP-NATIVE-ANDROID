package com.xtree.base.widget;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class BorwserWebView  extends WebChromeClient {
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return true;
    }
}
