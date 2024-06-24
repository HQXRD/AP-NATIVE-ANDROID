package com.xtree.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.xtree.base.R;
import com.xtree.base.utils.CfLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义折叠的TextView
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {
    private final int STATE_NOT_OVERFLOW = 1; //文本行数不超过限定行数
    private final int STATE_COLLAPSED = 2; //文本行数超过限定行数,处于折叠状态
    private final int STATE_EXPANDED = 3; //文本行数超过限定行数,被点击全文展开

    /* 默认最高行数 */
    private static final int MAX_COLLAPSED_LINES = 1;
    private Map<Integer, Integer> mCollapsedStatus = new HashMap<>();
    private TextView tv_expandable_content; //折叠状态TextView
    private TextView tv_expand_or_collapse;

    private boolean forceRefresh;//是否每次setText都重新获取文本行数
    private float textViewWidthPx;
    private int position;
    private ExpandStatusChangedListener expandStatusChangedListener;

    private ExpandStatusListener expandStatusListener;//打开/关闭状态回调接口
    private String closeString;//关闭状态文本
    private String openString;//打开状态文本
    private int textViewStatus = 0;//textView 打开关闭状态 0 为关闭；1为打开
    private String foldTxt;//折叠文本
    private boolean isSingleLine = false;//是否为单行

    /**
     * 获取textView 状态
     *
     * @return
     */
    public int getTextViewStatus() {
        return textViewStatus;
    }

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    public void setCollapsedStatus(HashMap<Integer, Integer> mCollapsedStatus) {
        this.mCollapsedStatus = mCollapsedStatus;
    }

    public void setTextViewWidthPx(float textViewWidthPx) {
        this.textViewWidthPx = textViewWidthPx;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    public void setExpandStatusChangedListener(ExpandStatusChangedListener expandStatusChangedListener) {
        this.expandStatusChangedListener = expandStatusChangedListener;
    }

    public void setExpandStatusListener(ExpandStatusListener expandStatusListener) {
        this.expandStatusListener = expandStatusListener;
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        setVisibility(GONE);
    }

    /**
     * 渲染完成时初始化view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    /**
     * 初始化view
     */
    private void findViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_expandable_textview, this);
        tv_expandable_content = findViewById(R.id.tv_expandable_content);
        tv_expand_or_collapse = findViewById(R.id.tv_expand_or_collapse);

        setOnClickListener(this);
    }

    public void setText(String text) {
        splitFoldString(text);
        this.setText(text, 0);
    }

    /**
     * 按照逗号切割字符
     *
     * @param text
     */
    private void splitFoldString(String text) {
        if (!text.contains(",")) {
            foldTxt = text;
            isSingleLine = true;
            return;
        }
        ///PT真人,BBIN真人,
        String subString = text.substring(0, text.length() - 1);
        String[] split = subString.split(",");
        if (split.length > 3) {
            foldTxt = split[0] + "," + split[1] + "," + split[2];
        } else if (split.length == 2) {
            foldTxt = split[0] + "," + split[1];
            isSingleLine = true;
        } else if (split.length == 1) {
            foldTxt = split[0];
            isSingleLine = true;
        } else {
            isSingleLine = true;
            foldTxt = subString;
        }

    }

    public void setText(String text, final int index) {
        if (forceRefresh) {
            mCollapsedStatus.put(index, null);
        }

        closeString = text;
        openString = text;

        this.position = index;
        Integer state = mCollapsedStatus.get(index);

        if (state == null) {
            int lineCount = getLineCount(text);
            if (lineCount > MAX_COLLAPSED_LINES) {
                tv_expandable_content.setMaxLines(MAX_COLLAPSED_LINES);
                tv_expand_or_collapse.setVisibility(View.VISIBLE);
                mCollapsedStatus.put(index, STATE_COLLAPSED);

            } else {
                tv_expandable_content.setMaxLines(lineCount);
                tv_expand_or_collapse.setVisibility(View.GONE);
                mCollapsedStatus.put(index, STATE_NOT_OVERFLOW);

            }
            // 设置字体变色 需要单独封装
          /*  String times;
            times = "<font color=#0C0319>" + foldTxt + "</font>";
            String testTxt = "<font color=#A17DF5>" + "查看" + "</font>";
            String format = getContext().getResources().getString(R.string.txt_withdraw_flow_close_tip);
            String textSource = String.format(format, times, testTxt);
            tv_expandable_content.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));*/
        } else {
            //如果之前已经初始化过了，则使用保存的状态，无需再获取一次
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    tv_expandable_content.setMaxLines(MAX_COLLAPSED_LINES);
                    tv_expand_or_collapse.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    tv_expandable_content.setMaxLines(MAX_COLLAPSED_LINES);
                    tv_expand_or_collapse.setVisibility(View.VISIBLE);
                    break;
                case STATE_EXPANDED:
                    tv_expandable_content.setMaxLines(Integer.MAX_VALUE);
                    tv_expand_or_collapse.setVisibility(View.VISIBLE);
                    break;
            }
            /*String times;
            times = "<font color=#0C0319>" + foldTxt + "</font>";
            String testTxt = "<font color=#A17DF5>" + "  查看" + "</font>";
            String format = getContext().getResources().getString(R.string.txt_withdraw_flow_close_tip);
            String textSource = String.format(format, times, testTxt);
            tv_expandable_content.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));*/
        }
        String times;
        if (isSingleLine){
            //单行文本 不显示查看字体
            times = "<font color=#121B47>" + foldTxt + "</font>";
            String format = getContext().getResources().getString(R.string.txt_withdraw_flow_close_single);
            String textSource = String.format(format, times);
            tv_expandable_content.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
        }else{
            times = "<font color=#121B47>" + foldTxt + "</font>";
            String testTxt = "<font color=#A8423A>" + "  查看" + "</font>";
            String format = getContext().getResources().getString(R.string.txt_withdraw_flow_close_tip);
            String textSource = String.format(format, times, testTxt);
            tv_expandable_content.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
        }


        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int state = mCollapsedStatus.get(position);
        //两种状态下对应动作
        if (state == STATE_COLLAPSED) {
            if (!isSingleLine){
                // 打开状态
                CfLog.e("state == STATE_COLLAPSED");
                tv_expandable_content.setMaxLines(Integer.MAX_VALUE);
                mCollapsedStatus.put(position, STATE_EXPANDED);

                if (null != expandStatusChangedListener) {
                    expandStatusChangedListener.onChanged(true);
                }
                //打开状态
                if (expandStatusListener != null) {
                    this.expandStatusListener.onStatusChange(true);
                }

                String times;
                times = "<font color=#121B47>" + openString + "</font>";
                String testTxt = "<font color=#A8423A>" + "收起" + "</font>";
                String format = getContext().getResources().getString(R.string.txt_withdraw_flow_open_tip);
                String textSource = String.format(format, times, testTxt);
                tv_expandable_content.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }


        } else if (state == STATE_EXPANDED) {
            //折叠状态
            CfLog.e("state == STATE_EXPANDED");
            if (!isSingleLine){
                tv_expandable_content.setMaxLines(MAX_COLLAPSED_LINES);
                mCollapsedStatus.put(position, STATE_COLLAPSED);

                if (null != expandStatusChangedListener) {
                    expandStatusChangedListener.onChanged(false);
                }
                //手动关闭状态
                if (expandStatusListener != null) {
                    this.expandStatusListener.onStatusChange(false);
                }

                String times;
                times = "<font color=#121B47>" + foldTxt + "</font>";
                String testTxt = "<font color=#A8423A>" + "查看" + "</font>";
                String format = getContext().getResources().getString(R.string.txt_withdraw_flow_close_tip);
                String textSource = String.format(format, times, testTxt);
                tv_expandable_content.setText(HtmlCompat.fromHtml(textSource, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        }
    }

    public int getLineCount(String text) {
        Paint paint = new Paint();
        paint.setTextSize(tv_expandable_content.getTextSize());
        paint.setTypeface(tv_expandable_content.getTypeface());

        List<String> strings = splitWordsIntoStringsThatFit(text, textViewWidthPx, paint);

        return strings.size();
    }

    public static List<String> splitWordsIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
        ArrayList<String> result = new ArrayList<>();

        ArrayList<String> currentLine = new ArrayList<>();

        String[] sources = source.split("\n");
        for (String chunk : sources) {
            if ("".equals(chunk)) {
                chunk = "\n";
            }
            if (paint.measureText(chunk) < maxWidthPx) {
                processFitChunk(maxWidthPx, paint, result, currentLine, chunk);
            } else {
                List<String> splitChunk = splitIntoStringsThatFit(chunk, maxWidthPx, paint);
                for (String chunkChunk : splitChunk) {
                    processFitChunk(maxWidthPx, paint, result, currentLine, chunkChunk);
                }
            }
        }

        if (!currentLine.isEmpty()) {
            result.addAll(currentLine);
        }
        return result;
    }

    private static List<String> splitIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
        if (TextUtils.isEmpty(source) || paint.measureText(source) <= maxWidthPx) {
            return Arrays.asList(source);
        }

        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        for (int i = 1; i <= source.length(); i++) {
            String substr = source.substring(start, i);
            if (paint.measureText(substr) >= maxWidthPx) {
                String fits = source.substring(start, i - 1);
                result.add(fits);
                start = i - 1;
            }
            if (i == source.length()) {
                String fits = source.substring(start, i);
                result.add(fits);
            }
        }

        return result;
    }

    private static void processFitChunk(float maxWidth, Paint paint, ArrayList<String> result, ArrayList<String> currentLine, String chunk) {
        currentLine.add(chunk);
        String currentLineStr = TextUtils.join(" ", currentLine);
        if (paint.measureText(currentLineStr) >= maxWidth || "\n".equals(chunk)) {
            currentLine.remove(currentLine.size() - 1);
            result.add(TextUtils.join(" ", currentLine));
            currentLine.clear();
            currentLine.add(chunk);
        }
    }

    public interface ExpandStatusChangedListener {

        void onChanged(boolean isExpand);

    }

    /**
     * ExpandableTextView打开/关闭状态回调接口
     * true:打开
     * false:关闭
     */
    public interface ExpandStatusListener {
        void onStatusChange(boolean isExpand);
    }
}
