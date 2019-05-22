package com.andy.tinker;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.btn_load)
    Button btnLoad;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_toast)
    Button btnToast;
    @BindView(R.id.btn_show)
    Button btnShow;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @OnClick({R.id.btn_load, R.id.btn_clear, R.id.btn_toast, R.id.btn_show})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                loadPath();
                break;

            case R.id.btn_clear:
                clearPath();
                break;

            case R.id.btn_toast:
                //修改前
                Toast.makeText(getApplicationContext(), "使用Tinker修改后的内容", Toast.LENGTH_SHORT).show();
                //修改后
                //Toast.makeText(getApplicationContext(), "使用Tinker修改后的内容，你把我改成什么了", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_show:
                showInfo(this);
                break;
        }
    }

    //加载补丁
    private void loadPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";
        File file = new File(path);
        if (file.exists()) {
            Toast.makeText(this, "补丁存在", Toast.LENGTH_SHORT).show();
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
        } else {
            Toast.makeText(this, "补丁不存在", Toast.LENGTH_SHORT).show();
        }
    }

    //清除补丁
    private void clearPath() {
        Tinker.with(getApplicationContext()).cleanPatch();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";
        File file = new File(path);
        if (file.exists()) {
            Toast.makeText(this, "补丁存在", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "补丁不存在", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean showInfo(Context context) {
        // add more Build Info
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", "1.9.1"));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", "1.9.1"));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", "I am the base apk"));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

        } else {
            sb.append(String.format("[patch is not loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", "1.9.1"));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", "1.9.1"));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", "I am the base apk"));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }
        sb.append(String.format("[BaseBuildInfo Message] %s \n", ""));

        final TextView v = new TextView(context);
        v.setText(sb);
        v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setTextColor(0xFF000000);
        v.setTypeface(Typeface.MONOSPACE);
        final int padding = 16;
        v.setPadding(padding, padding, padding, padding);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(v);
        final AlertDialog alert = builder.create();
        alert.show();
        return true;
    }

}
