package com.zhangke.shizhong.gdt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.felix.gdtlib.gdt.NativeAdListenerImpl;
import com.felix.gdtlib.helper.TencentAdHelper;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.squareup.picasso.Picasso;
import com.zhangke.shizhong.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Felix.Zhong on 2019/8/12 11:43
 * 原生广告通用弹窗(自渲染方式)
 */
public class NativeADCommonActivity extends Activity {

    /* **********原生广告,自渲染类型 **********/
    /**
     * 对话框,问答类型
     */
    public static final int NATIVE_AD_TYPE1 = 1;
    /**
     * 大屏右上关闭类型
     */
    public static final int NATIVE_AD_TYPE2 = 2;
    /**
     * 预留类型
     */
    public static final int NATIVE_AD_TYPE3 = 3;

    public static final String EXTRA_NATIVE_AD_TYPE = "NATIVE_AD_TYPE";
    public static final String EXTRA_OK_ACTION_TYPE = "OK_ACTION_TYPE";

    /**
     * 点击确定按钮时的动作
     */
    private int mOkActionType = -1;
    /**
     * 退出应用
     */
    public static final int OK_ACTION_TYPE_EXIT = 1;
    /**
     * 退出页面
     */
    public static final int OK_ACTION_TYPE_EXIT_PAGE = 1;
    /**
     * 开始下载
     */
    public static final int OK_ACTION_TYPE_DOWNLOAD = 2;

    @BindView(R.id.img_poster)
    ImageView imgPoster;
    @BindView(R.id.ibtn_close)
    ImageView ibtnClose;


    public static void showNativeADDialog(Context context, int nativeAdType, int okActionType) {
        Intent intent = new Intent(context, NativeADCommonActivity.class);
        intent.putExtra(EXTRA_NATIVE_AD_TYPE, nativeAdType);
        intent.putExtra(EXTRA_OK_ACTION_TYPE, okActionType);
        ActivityUtils.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //外部触摸不能关闭
        setFinishOnTouchOutside(false);
        int nativeAdType = getIntent().getIntExtra(EXTRA_NATIVE_AD_TYPE, 3);
        mOkActionType = getIntent().getIntExtra(EXTRA_OK_ACTION_TYPE, -1);
        switch (nativeAdType) {
            case NATIVE_AD_TYPE1:
                setContentView(R.layout.activity_native_ad);
                loadType1NativeAD();
                break;
            case NATIVE_AD_TYPE2:
                setContentView(R.layout.activity_native_ad6);
                loadType2NativeAD();
                break;
            case NATIVE_AD_TYPE3:
            default:
                setContentView(R.layout.activity_native_ad3);
                break;
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        View.OnClickListener onClickListener = v -> closeDialog();
        ibtnClose.setOnClickListener(onClickListener);
    }

    /**
     * 加载类型1原生广告,有确认会话
     */
    private void loadType1NativeAD() {
        Button btnNo = findViewById(R.id.btn_no);
        Button btnYes = findViewById(R.id.btn_yes);
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText("是否要退出应用?");
        btnNo.setOnClickListener(v -> closeDialog());
        btnYes.setOnClickListener(v -> {
            if (mOkActionType == OK_ACTION_TYPE_EXIT) {
                AppUtils.exitApp();
            }
        });
        TencentAdHelper.getInstance().initAndLoadNativeAD(getApplicationContext(),
                TencentAdHelper.getInstance().getTencentAdIDs().getNATIVE_POSID_TYPE_SELF_RENDER_1_1(), new NativeAdListenerImpl() {
                    @Override
                    public void onADLoaded(List<NativeADDataRef> list) {
                        super.onADLoaded(list);
                        if (list != null && list.size() > 0) {
                            NativeADDataRef nativeADDataRef = list.get(0);
                            String imgUrl = nativeADDataRef.getImgUrl();
                            String iconUrl = nativeADDataRef.getIconUrl();
                            String title = nativeADDataRef.getTitle();
                            String desc = nativeADDataRef.getDesc();

                            //tvMessage.setText(desc);
                            Picasso.get().load(imgUrl).into(imgPoster);
                            //必须先曝光再点击才有效
                            nativeADDataRef.onExposured(imgPoster);
                            nativeADDataRef.onExposured(tvMessage);
                            imgPoster.setOnClickListener(v -> {
                                nativeADDataRef.onClicked(imgPoster);
                                closeDialog();
                            });
                            tvMessage.setOnClickListener(v -> {
                                nativeADDataRef.onClicked(tvMessage);
                                closeDialog();
                            });
                        }
                    }
                });
    }

    /**
     * 加载类型1原生广告,有确认会话
     */
    private void loadType2NativeAD() {
        TextView tvMessage = findViewById(R.id.tv_message);
        TencentAdHelper.getInstance().initAndLoadNativeAD(getApplicationContext(),
                TencentAdHelper.getInstance().getTencentAdIDs().getNATIVE_POSID_TYPE_SELF_RENDER_1_2(), new NativeAdListenerImpl() {
                    @Override
                    public void onADLoaded(List<NativeADDataRef> list) {
                        super.onADLoaded(list);
                        if (list != null && list.size() > 0) {
                            NativeADDataRef nativeADDataRef = list.get(0);
                            String imgUrl = nativeADDataRef.getImgUrl();
                            String iconUrl = nativeADDataRef.getIconUrl();
                            String title = nativeADDataRef.getTitle();
                            String desc = nativeADDataRef.getDesc();


                            tvMessage.setText(title);
                            Picasso.get().load(imgUrl).into(imgPoster);
                            //必须先曝光再点击才有效
                            nativeADDataRef.onExposured(imgPoster);
                            imgPoster.setOnClickListener(v -> {
                                nativeADDataRef.onClicked(imgPoster);
                                closeDialog();
                            });
                        }
                    }
                });
    }

    /**
     * 关闭对话框
     */
    private void closeDialog() {
        ActivityUtils.finishActivity(NativeADCommonActivity.this, R.anim.dialog_in, R.anim.dialog_out);
    }
}
