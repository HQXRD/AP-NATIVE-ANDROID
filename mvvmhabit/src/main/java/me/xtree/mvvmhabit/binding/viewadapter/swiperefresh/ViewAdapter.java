package me.xtree.mvvmhabit.binding.viewadapter.swiperefresh;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import me.xtree.mvvmhabit.binding.command.BindingCommand;


/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    //下拉刷新命令
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final BindingCommand onRefreshCommand) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (onRefreshCommand != null) {
                onRefreshCommand.execute();
            }
        });
    }

    //是否刷新中
    @BindingAdapter({"refreshing"})
    public static void setRefreshing(SwipeRefreshLayout swipeRefreshLayout, boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

}
