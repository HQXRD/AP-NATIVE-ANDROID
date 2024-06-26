package com.xtree.base.widget;

import android.content.Context;

import androidx.annotation.NonNull;

public class MsgDialog extends TipDialog {

    public MsgDialog(@NonNull Context context, String title, String msg, ICallBack mCallBack) {
        super(context, title, msg, mCallBack);
    }

    public MsgDialog(@NonNull Context context, String title, String msg, String txtLeft, String txtRight, ICallBack mCallBack) {
        super(context, title, msg, txtLeft, txtRight, mCallBack);
    }

    public MsgDialog(@NonNull Context context, String title, CharSequence msg, boolean isSingleBtn, ICallBack mCallBack) {
        super(context, title, msg, isSingleBtn, mCallBack);
    }

    public MsgDialog(@NonNull Context context, CharSequence title, CharSequence title2, CharSequence msg, String txtLeft, String txtRight, boolean isSingleBtn, ICallBack mCallBack) {
        super(context, title, title2, msg, txtLeft, txtRight, isSingleBtn, mCallBack);
    }

}
