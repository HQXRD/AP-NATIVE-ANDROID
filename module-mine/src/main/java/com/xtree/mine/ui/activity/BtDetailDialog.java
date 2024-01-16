package com.xtree.mine.ui.activity;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBtDetailBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.vo.BtDetailVo;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.Utils;

public class BtDetailDialog extends BottomPopupView {

    private Context ctx;
    LifecycleOwner owner;
    private String gameCode;
    private String platform; // 平台编码 OBGQP (请求接口用)
    private String platformName; // 平台名 (显示用)

    ReportViewModel viewModel;
    DialogBtDetailBinding binding;

    private BtDetailDialog(@NonNull Context context) {
        super(context);
    }

    //public BtDetailDialog(@NonNull Context context, Context ctx, String gameCode, String platform) {
    //    super(context);
    //    this.ctx = ctx;
    //    this.gameCode = gameCode;
    //    this.platform = platform;
    //}

    /**
     * @param ctx
     * @param owner
     * @param gameCode
     * @param code
     * @param platformName
     * @return
     */
    public static BtDetailDialog newInstance(Context ctx, LifecycleOwner owner, String gameCode, String code, String platformName) {
        BtDetailDialog dialog = new BtDetailDialog(ctx);
        dialog.ctx = ctx;
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

        requestData();
    }

    private void initView() {
        binding = DialogBtDetailBinding.bind(findViewById(R.id.ll_root));
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
            //binding.tvwSum.setActivated(true);
        } else if (vo.project_BetResult.equals("L")) {
            binding.tvwBtResult.setText(R.string.txt_rst_lose);
            binding.tvwBtResult.setSelected(false);
            //binding.tvwSum.setSelected(false);
        } else if (vo.project_BetResult.equals("W")) {
            binding.tvwBtResult.setText(R.string.txt_rst_win);
            binding.tvwBtResult.setSelected(true);
            //binding.tvwSum.setSelected(true);
        } else {
            binding.tvwBtResult.setText(R.string.txt_unsettle); // 未结算
            binding.tvwBtResult.setActivated(true);
            //binding.tvwSum.setActivated(true);
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
            binding.llAdvanceSettle.setVisibility(View.VISIBLE);
            int resTxt = vo.project_advance_settle.equals("1") ? R.string.txt_yes : R.string.txt_no;
            binding.tvwAdvanceSettle.setText(resTxt);
        }

        // content 类型会变化, 大部分情况是BtContentVo, 少数情况是String.
        if (vo.content instanceof Map) {
            BtDetailVo.BtContentVo mBtContentVo = new Gson().fromJson(new Gson().toJson(vo.content), BtDetailVo.BtContentVo.class);
            if (!mBtContentVo.list.isEmpty()) {
                BtDetailVo.BtContentItemVo t = mBtContentVo.list.get(0);
                setBtContent(t);
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
            binding.tvwBetContent.setText(t.bet_content + "\n" + t.competition_name + "\n" + t.game_type + "\n" + t.match_name);
            //binding.tvwBetContent.setText(t.bet_content);
            //binding.tvwCompetitionName.setText(t.competition_name);
            //binding.tvwGameType.setText(t.game_type);
            //binding.tvwMatchName.setText(t.match_name);
        } else {
            // 比赛类 有8个字段
            binding.llDetail.llRoot.setVisibility(View.VISIBLE); // 显示出来
            binding.llDetail.tvwSportsName.setText(t.SportsName);
            binding.llDetail.tvwCompetitionName.setText(t.competition_name);
            binding.llDetail.tvwTeam.setText(t.team);
            binding.llDetail.tvwEventDateTime.setText(t.EventDateTime);
            binding.llDetail.tvwBetType.setText(t.BetType);
            binding.llDetail.tvwPlaycontent.setText(t.playcontent);
            binding.llDetail.tvwBetContent.setText(t.bet_content);
            binding.llDetail.tvwOdds.setText(t.Odds);
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
