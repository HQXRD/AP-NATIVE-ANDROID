package com.xtree.mine.ui.activity;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBtDetailBinding;
import com.xtree.mine.databinding.LayoutBtDetailMatchBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.vo.BtDetailVo;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.Utils;

public class BtDetailDialog extends BottomPopupView {

    LifecycleOwner owner;
    private String gameCode;
    private String platform; // 平台编码 OBGQP (请求接口用)
    private String platformName; // 平台名 (显示用)

    private ReportViewModel viewModel;
    private DialogBtDetailBinding binding;

    private BtDetailDialog(@NonNull Context context) {
        super(context);
    }

    public static BtDetailDialog newInstance(Context ctx, LifecycleOwner owner, String gameCode, String code, String platformName) {
        BtDetailDialog dialog = new BtDetailDialog(ctx);
        dialog.owner = owner;
        dialog.gameCode = gameCode;
        dialog.platform = code;
        dialog.platformName = platformName;

        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
        initData();
        initViewObservable();

        LoadingDialog.show(getContext());
        requestData();
    }

    private void initView() {
        binding = DialogBtDetailBinding.bind(findViewById(R.id.cl_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
    }

    private void initData() {
        viewModel = new ReportViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        viewModel.liveDataBtDetail.observe(owner, vo -> {
            CfLog.i();
            setView(vo);
        });
    }

    private void setView(BtDetailVo vo) {

        // T-和,L-输,W-胜, 其它-未结算
        if (vo.project_BetResult.equals("T")) {
            binding.tvwBtResult.setText(R.string.txt_rst_tie);
            binding.tvwBtResult.setActivated(true);
        } else if (vo.project_BetResult.equals("L")) {
            binding.tvwBtResult.setText(R.string.txt_rst_lose);
            binding.tvwBtResult.setSelected(false);
        } else if (vo.project_BetResult.equals("W")) {
            binding.tvwBtResult.setText(R.string.txt_rst_win);
            binding.tvwBtResult.setSelected(true);
        } else {
            binding.tvwBtResult.setText(R.string.txt_unsettle); // 未结算
            binding.tvwBtResult.setActivated(true);
        }
        //String win = vo.project_win.equals("0") ? "--" : vo.project_win;

        binding.tvwUsername.setText(vo.project_username);
        binding.tvwVip.setText(vo.vip);
        binding.tvwGameCode.setText(vo.project_Game_code);
        binding.tvwGameDate.setText(vo.project_Game_date);
        binding.tvwVenue.setText(platformName); // OBGQP vo.venue
        binding.tvwGameName.setText(vo.project_Game_name);
        binding.tvwBet.setText(vo.project_bet);
        binding.tvwWin.setText(vo.project_win); //
        binding.tvwWinlostdatetime.setText(vo.project_WinLostDateTime);

        // 是否提前结算
        if (!TextUtils.isEmpty(vo.project_advance_settle)) {
            binding.clAdvanceSettle.setVisibility(View.VISIBLE);
            int resTxt = vo.project_advance_settle.equals("1") ? R.string.txt_yes : R.string.txt_no;
            binding.tvwAdvanceSettle.setText(resTxt);
        }

        // content 类型会变化, 大部分情况是BtContentVo, 少数情况是String.
        if (vo.content instanceof Map) {
            BtDetailVo.BtContentVo mBtContentVo = new Gson().fromJson(new Gson().toJson(vo.content), BtDetailVo.BtContentVo.class);
            if (mBtContentVo.list != null && !mBtContentVo.list.isEmpty()) {
                for (int i = 0; i < mBtContentVo.list.size(); i++) {
                    BtDetailVo.BtContentItemVo t = mBtContentVo.list.get(i);
                    setBtContent(t);
                }
            }
        } else if (vo.content instanceof String) {
            CfLog.i(vo.content.toString());
            binding.llBetContent.setVisibility(View.VISIBLE); // 显示出来
            binding.tvwBetContent.setText(vo.content.toString()); // 少数情况会出现
        } else {
            CfLog.e(vo.content.toString()); // 其它格式,暂未发现
        }
    }

    private void setBtContent(BtDetailVo.BtContentItemVo t) {

        if (TextUtils.isEmpty(t.SportsName)) {
            // 有4个字段
            binding.llBetContent.setVisibility(View.VISIBLE); // 显示出来
            binding.tvwBetContent.append(t.bet_content + "\n" + t.competition_name + "\n" + t.game_type + "\n" + t.match_name + "\n\n");
            //binding.tvwBetContent.setText(t.bet_content);
            //binding.tvwCompetitionName.setText(t.competition_name);
            //binding.tvwGameType.setText(t.game_type);
            //binding.tvwMatchName.setText(t.match_name);
        } else {
            // 比赛类 有8个字段
            //binding.llDetail.llRoot.setVisibility(View.VISIBLE); // 显示出来
            //binding.llDetail.tvwSportsName.setText(t.SportsName);
            //binding.llDetail.tvwCompetitionName.setText(t.competition_name);
            //binding.llDetail.tvwTeam.setText(t.team);
            //binding.llDetail.tvwEventDateTime.setText(t.EventDateTime);
            //binding.llDetail.tvwBetType.setText(t.BetType);
            //binding.llDetail.tvwPlaycontent.setText(t.playcontent);
            //binding.llDetail.tvwBetContent.setText(t.bet_content);
            //binding.llDetail.tvwOdds.setText(t.Odds);

            LayoutBtDetailMatchBinding binding2 = LayoutBtDetailMatchBinding.inflate(LayoutInflater.from(getContext()));
            binding2.llRoot.setVisibility(View.VISIBLE); // 显示出来
            binding2.tvwSportsName.setText(t.SportsName);
            binding2.tvwCompetitionName.setText(t.competition_name);
            binding2.tvwTeam.setText(t.team);
            binding2.tvwEventDateTime.setText(t.EventDateTime);
            binding2.tvwBetType.setText(t.BetType);
            binding2.tvwPlaycontent.setText(t.playcontent);
            binding2.tvwBetContent.setText(t.bet_content);
            binding2.tvwOdds.setText(t.Odds);

            binding.llMain.addView(binding2.getRoot());

        }

    }

    private void requestData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("project_id", gameCode); // 24011202****4286
        map.put("platform", platform); // OBGQP
        map.put("nonce", UuidUtil.getID16());
        viewModel.getBtOrderDetail(map);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bt_detail;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

}
