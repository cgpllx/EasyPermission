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
                // ����Ȩ�ޡ�
                EasyPermission.with(RationalePermissionActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                                // rationale�����ǣ��û��ܾ�һ��Ȩ�ޣ��ٴ�����ʱ�������û�ͬ�⣬�ٴ���Ȩ�Ի��򣬱����û���ѡ������ʾ��
                        .rationale(rationaleListener)
                        .send();
            }
        });
    }

    /**
     * Rationale֧�֣������Զ���Ի���
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {

            // ����ʹ���Զ���Ի�����������Զ��壬��AndPermissionĬ�϶Ի���
            // EasyPermission.rationaleDialog(Context, Rationale).show();

            // �Զ���Ի���
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
//            SettingService settingHandle = EasyPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            ���dialog�����ȷ�����ã�
//            settingHandle.execute();
//            ���dialog�����ȡ�����ã�
//            settingHandle.cancel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // listener��ʽ�����һ��������PermissionListener��
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
