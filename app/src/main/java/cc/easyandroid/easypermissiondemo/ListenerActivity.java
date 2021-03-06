package cc.easyandroid.easypermissiondemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cc.easyandroid.easypermission.EasyPermission;
import cc.easyandroid.easypermission.PermissionListener;
import cc.easyandroid.easypermission.Rationale;
import cc.easyandroid.easypermission.RationaleListener;

public class ListenerActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {

    private static final int REQUEST_CODE_PERMISSION_SD = 100;
    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;

    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_request_single).setOnClickListener(this);
        findViewById(R.id.btn_request_multi).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_single: {
                // 申请单个权限。
                EasyPermission.with(this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(Manifest.permission.WRITE_CALENDAR)
                                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                EasyPermission.rationaleDialog(ListenerActivity.this, rationale).show();
                            }
                        }).send();
                break;
            }
            case R.id.btn_request_multi: {
                // 申请多个权限。
                EasyPermission.with(this)
                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                        .permission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS)
                                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                EasyPermission.rationaleDialog(ListenerActivity.this, rationale).show();
                            }
                        }).send();
                break;
            }
        }
    }

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_SD: {
                Toast.makeText(this, R.string.message_calendar_succeed, Toast.LENGTH_SHORT).show();
                break;
            }
            case REQUEST_CODE_PERMISSION_OTHER: {
                Toast.makeText(this, R.string.message_post_succeed, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_SD: {
                Toast.makeText(this, R.string.message_calendar_failed, Toast.LENGTH_SHORT).show();
                break;
            }
            case REQUEST_CODE_PERMISSION_OTHER: {
                Toast.makeText(this, R.string.message_post_failed, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        // 用户勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (EasyPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            EasyPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

            // 第二种：用自定义的提示语。
//             EasyPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingService = EasyPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingService.execute();
//            你的dialog点击了取消调用：
//            settingService.cancel();
        }
    }

    //----------------------------------鏉冮檺鍥炶皟澶勭悊----------------------------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         * @param listener PermissionListener 对象。
         */
        EasyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(this, R.string.message_setting_back, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}
