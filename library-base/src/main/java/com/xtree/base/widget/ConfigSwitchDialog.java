package com.xtree.base.widget;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.xtree.base.R;
import com.xtree.base.databinding.DialogConfigSwitchBinding;
import com.xtree.base.global.SPKeyGlobal;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/4/20.
 * Describe:
 */
public class ConfigSwitchDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogConfigSwitchBinding binding = DialogConfigSwitchBinding.inflate(getLayoutInflater());

        int domainMode = SPUtils.getInstance().getInt(SPKeyGlobal.DOMAIN_MODE,0);

        switch (domainMode) {
            case 0:
            case 1://正式环境
                binding.radioGroup.check(binding.relase.getId());
                break;

            case 2://测试环境
                binding.radioGroup.check(binding.test.getId());
                break;
        }
        
        binding.confirmed.setOnClickListener(v -> {
            int checkedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();

            if (checkedRadioButtonId == binding.relase.getId()) {
                SPUtils.getInstance().put(SPKeyGlobal.DOMAIN_MODE, 1);
            } else if (checkedRadioButtonId == binding.test.getId()) {
                SPUtils.getInstance().put(SPKeyGlobal.DOMAIN_MODE, 2);
            }

//            relaunchApp(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppManager.getAppManager().AppExit();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }, 500);

        });
        return binding.getRoot();
    }

    private void relaunchApp(final boolean isKillProcess) {
        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
        if (intent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.");
            return;
        }
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        if (!isKillProcess) return;
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
