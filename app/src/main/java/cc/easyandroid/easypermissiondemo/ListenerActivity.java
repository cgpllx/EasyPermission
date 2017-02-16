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
                // ���뵥��Ȩ�ޡ�
                EasyPermission.with(this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(Manifest.permission.WRITE_CALENDAR)
                                // rationale�����ǣ��û��ܾ�һ��Ȩ�ޣ��ٴ�����ʱ�������û�ͬ�⣬�ٴ���Ȩ�Ի��򣬱����û���ѡ������ʾ��
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // ����ĶԻ�������Զ��壬ֻҪ����rationale.resume()�Ϳ��Լ������롣
                                EasyPermission.rationaleDialog(ListenerActivity.this, rationale).show();
                            }
                        }).send();
                break;
            }
            case R.id.btn_request_multi: {
                // ������Ȩ�ޡ�
                EasyPermission.with(this)
                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                        .permission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS)
                                // rationale�����ǣ��û��ܾ�һ��Ȩ�ޣ��ٴ�����ʱ�������û�ͬ�⣬�ٴ���Ȩ�Ի��򣬱����û���ѡ������ʾ��
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // ����ĶԻ�������Զ��壬ֻҪ����rationale.resume()�Ϳ��Լ������롣
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

        // �û���ѡ�˲�����ʾ���Ҿܾ���Ȩ�ޣ���ô��ʾ�û�����������Ȩ��
        if (EasyPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // ��һ�֣���Ĭ�ϵ���ʾ�
            EasyPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

            // �ڶ��֣����Զ������ʾ�
//             EasyPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("Ȩ������ʧ��")
//                     .setMessage("������Ҫ��һЩȨ�ޱ����ܾ�����ϵͳ������������ʧ�ܣ�����������ҳ���ֶ���Ȩ���������޷�����ʹ�ã�")
//                     .setPositiveButton("�ã�ȥ����")
//                     .show();

//            �����֣��Զ���dialog��ʽ��
//            SettingService settingService = EasyPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            ���dialog�����ȷ�����ã�
//            settingService.execute();
//            ���dialog�����ȡ�����ã�
//            settingService.cancel();
        }
    }

    //----------------------------------权限回调处理----------------------------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * ת��AndPermission���������
         *
         * @param requestCode  �����롣
         * @param permissions  Ȩ�����飬һ�����߶����
         * @param grantResults ��������
         * @param listener PermissionListener ����
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
