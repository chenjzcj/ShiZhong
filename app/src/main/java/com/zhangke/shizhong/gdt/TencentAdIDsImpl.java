package com.zhangke.shizhong.gdt;

import com.blankj.utilcode.util.SPUtils;
import com.felix.gdtlib.gdt.GDTConstant;
import com.felix.gdtlib.helper.TencentAdIDs;
import com.zhangke.shizhong.BuildConfig;

/**
 * Created by Felix.Zhong on 2019/8/2 15:43
 * 广点通广告媒体及广告位ID实现类
 */
public class TencentAdIDsImpl extends TencentAdIDs {

    @Override
    public String getAPPID() {
        return SPUtils.getInstance().getString(GDTConstant.APPID, BuildConfig.APPID);
    }

    @Override
    public String getBANNER2_POSID() {
        return SPUtils.getInstance().getString(GDTConstant.BANNER2_POSID, BuildConfig.BANNER2_POSID);
    }

    @Override
    public String getSPLASH_POSID() {
        return SPUtils.getInstance().getString(GDTConstant.SPLASH_POSID, BuildConfig.SPLASH_POSID);
    }

    @Override
    public String getNATIVE_POSID_INFO_FLOW() {
        return SPUtils.getInstance().getString(GDTConstant.NATIVE_POSID_INFO_FLOW, BuildConfig.NATIVE_POSID_INFO_FLOW);
    }

    @Override
    public String getNATIVE_POSID_TYPE_TPL() {
        return SPUtils.getInstance()
                .getString(GDTConstant.NATIVE_POSID_TYPE_TPL, BuildConfig.NATIVE_POSID_TYPE_TPL);
    }

    @Override
    public String getNATIVE_POSID_TYPE_TPL_2() {
        return SPUtils.getInstance()
                .getString(GDTConstant.NATIVE_POSID_TYPE_TPL_2, BuildConfig.NATIVE_POSID_TYPE_TPL_2);
    }

    @Override
    public String getNATIVE_POSID_TYPE_SELF_RENDER_1_1() {
        return SPUtils.getInstance()
                .getString(GDTConstant.NATIVE_POSID_TYPE_SELF_RENDER_1_1, BuildConfig.NATIVE_POSID_TYPE_SELF_RENDER_1_1);
    }

    @Override
    public String getNATIVE_POSID_TYPE_SELF_RENDER_1_2() {
        return SPUtils.getInstance()
                .getString(GDTConstant.NATIVE_POSID_TYPE_SELF_RENDER_1_2, BuildConfig.NATIVE_POSID_TYPE_SELF_RENDER_1_2);
    }

    @Override
    public String getNATIVE_POSID_TYPE_SELF_RENDER_2() {
        return SPUtils.getInstance()
                .getString(GDTConstant.NATIVE_POSID_TYPE_SELF_RENDER_2, BuildConfig.NATIVE_POSID_TYPE_SELF_RENDER_2);
    }

    @Override
    public String getINTERSTITIAL_POSID_2() {
        return SPUtils.getInstance()
                .getString(GDTConstant.INTERSTITIAL_POSID_2, BuildConfig.INTERSTITIAL_POSID_2);
    }

    @Override
    public String getREWARD_VIDEO_POSID() {
        return SPUtils.getInstance()
                .getString(GDTConstant.REWARD_VIDEO_POSID, BuildConfig.REWARD_VIDEO_POSID);
    }
}
