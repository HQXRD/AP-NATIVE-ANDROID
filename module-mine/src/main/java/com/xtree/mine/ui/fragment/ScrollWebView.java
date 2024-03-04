package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xtree.base.utils.CfLog;

import java.util.Map;

/*修正WebView 与 ScrollView 共同使用 滑动冲突*/
public class ScrollWebView extends WebView {

    public interface  IScrollWebView{
        public void  scrollWebViewStartLoad();
    }
    public IScrollWebView iScrollWebView ;
    public ScrollWebView(@NonNull Context context) {
        this(context, null);
    }

    public ScrollWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context , attrs , 0);
    }

    public ScrollWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
  /*  public void loadUrl(@android.annotation.NonNull String url) {
        checkThread();
        mProvider.loadUrl(url);
    }*/
    public void scrollWebViewLoadUrl( String url, Map<String, String> additionalHttpHeaders){
        if (iScrollWebView !=null){
            iScrollWebView.scrollWebViewStartLoad();
        }
        CfLog.i("iScrollWebView.scrollWebViewStartLoad() url = " +url);
        super.loadUrl(url,additionalHttpHeaders);
    }

    private void init(){
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);


        //settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportZoom(true);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //小米11  1400*3200 银行卡提款H5页面底部Button 抖动 暂时先将Integer.MAX_VALUE 设置为3
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>(2) , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }*/
}
