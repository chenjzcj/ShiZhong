package com.zhangke.shizhong.gdt;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.felix.commonlibrary.rx.RxjavaHelper;
import com.felix.gdtlib.gdt.SplashADListenerImpl;
import com.felix.gdtlib.helper.TencentAdHelper;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.zhangke.shizhong.BuildConfig;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.main.LaunchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Felix.Zhong on 2018-7-30 17:46:17
 * 带开屏广告的应用启动页
 */
public class SplashWithAdActivity extends AppCompatActivity {

    /**
     * 是否是第一次打开APP，如果第一次，需要进入引导页面，默认为true
     */
    public static final String IS_FIRST_OPEN_APP = "is_first_open_app";

    FrameLayout flAdContainer;
    RelativeLayout rlBottomLogo;
    TextView mTvSkip;

    /**
     * SHOW_AD = false仅仅作为测试，后面设置为true
     */
    private static final boolean SHOW_AD = true;

    private static final String SKIP = "跳过 %d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindowLayout(getWindow());
        setContentView(R.layout.activity_splash_with_ad);
        initView();
        if (SHOW_AD) {
            // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），
            // 那么只需要在这里直接调用fetchSplashAD接口。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkAndRequestPermission();
            } else {
                fetchSplashAD();
            }
        } else {
            enterHomeActivity(1);
        }
    }

    private void initView() {
        flAdContainer = findViewById(R.id.fl_ad_container);
        rlBottomLogo = findViewById(R.id.rl_bottom_logo);
        mTvSkip = findViewById(R.id.tv_skip);
        mTvSkip.setOnClickListener(v -> enterHomeActivity(0));

        if ("1".equals(BuildConfig.HIDDEN_BOTTOM_LOGO)) {
            rlBottomLogo.setVisibility(View.GONE);
        }
    }

    /**
     * 水滴屏全屏适配方法（华为Mate20手机）
     * https://blog.csdn.net/weixin_37997371/article/details/83536953
     * <p>
     * 在Activity里面通过getWindow()获取window参数，然后再onCreate()函数里面调用下面的函数
     */
    public static void setFullScreenWindowLayout(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //设置页面全屏显示
        WindowManager.LayoutParams lp = window.getAttributes();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }*/
        //设置页面延伸到刘海区显示
        window.setAttributes(lp);
    }

    /**
     * 进入主界面
     */
    private void enterHomeActivity(long delay) {
        RxjavaHelper.delay(() -> {
            boolean isFirstOpenApp = SPUtils.getInstance().getBoolean(IS_FIRST_OPEN_APP, true);
            //ActivityUtils.startActivity(isFirstOpenApp ? GuideActivity.class : HomeActivity.class);
            ActivityUtils.startActivity(LaunchActivity.class);
            finish();
        }, delay);
    }

    private void fetchSplashAD() {
        SplashADListener listener = new SplashADListenerImpl() {

            //广告结束
            @Override
            public void onADDismissed() {
                enterHomeActivity(0);
            }

            //广告加载失败
            @Override
            public void onNoAD(AdError adError) {
                LogUtils.i("adError msg= " + adError.getErrorMsg() + ",code = " + adError.getErrorCode());
                enterHomeActivity(3);
            }

            // 广告展示后一定要把预设的开屏图片隐藏起来
            @Override
            public void onADPresent() {
                if (mTvSkip != null) {
                    mTvSkip.setVisibility(View.VISIBLE);
                }
            }

            //广告倒计时
            @Override
            public void onADTick(long millisUntilFinished) {
                if (mTvSkip != null) {
                    mTvSkip.setText(String.format(Locale.CHINA, SKIP, Math.round(millisUntilFinished / 1000f)));
                }
            }
        };
        TencentAdHelper.getInstance().loadSplashAD(this, flAdContainer, mTvSkip, listener);
    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            fetchSplashAD();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            fetchSplashAD();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }


    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
