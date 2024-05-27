package com.xtree.base.widget;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;

/**
 * 403页面 <br />
 * xxx/error/ap/403.html
 */
@Route(path = RouterActivityPath.Widget.PAGER_FORBIDDEN)
public class ForbiddenActivity extends BrowserActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CfLog.i("******");
    }

}
