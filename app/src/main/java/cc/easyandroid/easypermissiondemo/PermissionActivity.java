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
import cc.easyandroid.easypermission.PermissionNo;
import cc.easyandroid.easypermission.PermissionYes;
import cc.easyandroid.easypermission.Rationale;
import cc.easyandroid.easypermission.RationaleListener;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

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
                                // rationale�����ǣ��û��ܾ�һ��Ȩ�ޣ��ٴ�����ʱ�������û�ͬ�⣬�ٴ���Ȩ�Ի��򣬱����û���ѡ������ʾ��
                        .permission(Manifest.permission.WRITE_CALENDAR).rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // ����ĶԻ�������Զ��壬ֻҪ����rationale.resume()�Ϳ��Լ������롣
                        EasyPermission.rationaleDialog(PermissionActivity.this, rationale).show();
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
                                EasyPermission.rationaleDialog(PermissionActivity.this, rationale).show();
                            }
                        }).send();
                break;
            }
        }
    }


    //----------------------------------������дȨ��----------------------------------//

    /**
     * <p>Ȩ��ȫ������ɹ��Ż�ص��������������ص�ʧ�ܵķ�����</p>
     * ����Ȩ������ɹ���ʹ��@PermissionYes(RequestCode)ע�⡣
     *
     * @param grantedPermissions AndPermission�ص�����������ɹ���Ȩ�ޡ�
     */
    @PermissionYes(REQUEST_CODE_PERMISSION_SD)
    private void getCalendarYes(List<String> grantedPermissions) {
        Toast.makeText(this, R.string.message_calendar_succeed, Toast.LENGTH_SHORT).show();
    }

    /**
     * <p>ֻҪ��һ��Ȩ������ʧ�ܾͻ�ص�������������Ҳ���ص��ɹ��ķ�����</p>
     * ����Ȩ������ʧ�ܣ�ʹ��@PermissionNo(RequestCode)ע�⡣
     *
     * @param deniedPermissions AndPermission�ص�����������ʧ�ܵ�Ȩ�ޡ�
     */
    @PermissionNo(REQUEST_CODE_PERMISSION_SD)
    private void getCalendarNo(List<String> deniedPermissions) {
        Toast.makeText(this, R.string.message_calendar_failed, Toast.LENGTH_SHORT).show();
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

    //----------------------------------联系人�?短信权限----------------------------------//


    @PermissionYes(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiYes(List<String> grantedPermissions) {
        Toast.makeText(this, R.string.message_post_succeed, Toast.LENGTH_SHORT).show();
    }

    @PermissionNo(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiNo(List<String> deniedPermissions) {
        Toast.makeText(this, R.string.message_post_failed, Toast.LENGTH_SHORT).show();

        // �û���ѡ�˲�����ʾ���Ҿܾ���Ȩ�ޣ���ô��ʾ�û�����������Ȩ��
        if (EasyPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            EasyPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle(R.string.title_dialog)
                    .setMessage(R.string.message_permission_failed)
                    .setPositiveButton(R.string.btn_dialog_yes_permission)
                    .setNegativeButton(R.string.btn_dialog_no_permission, null)
                    .show();

            // �����Զ�dialog���뿴���档
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
         * @param object     Ҫ���ܽ����Activity��Fragment��
         * @param requestCode  �����롣
         * @param permissions  Ȩ�����飬һ�����߶����
         * @param grantResults ��������
         */
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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
