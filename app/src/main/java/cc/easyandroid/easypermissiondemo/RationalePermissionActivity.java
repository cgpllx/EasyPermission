package cc.easyandroid.easypermissiondemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class RationalePermissionActivity extends AppCompatActivity implements PermissionListener {

    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;

    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rationale_permission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.btn_request_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 申请权限。
                EasyPermission.with(RationalePermissionActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener)
                        .send();
            }
        });
    }

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {

            // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
            // EasyPermission.rationaleDialog(Context, Rationale).show();

            // 自定义对话框。
            new AlertDialog.Builder(RationalePermissionActivity.this)
                    .setTitle(R.string.title_dialog)
                    .setMessage(R.string.message_permission_rationale)
                    .setPositiveButton(R.string.btn_dialog_yes_permission, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();
                        }
                    }).setNegativeButton(R.string.btn_dialog_no_permission, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    rationale.cancel();
                }
            }).show();
        }
    };

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION: {
                Toast.makeText(this, R.string.message_location_succeed, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION: {
                Toast.makeText(this, R.string.message_location_failed, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
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
//            SettingService settingHandle = EasyPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // listener方式，最后一个参数是PermissionListener。
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
