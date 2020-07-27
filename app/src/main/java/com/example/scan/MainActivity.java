package com.example.scan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.king.zxing.CaptureActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity   implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks{
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    private static final String[] NECESSARY_PERMISSION = {
             Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        onPermissionGranted();
    }

    @AfterPermissionGranted(100)
    public void onPermissionGranted() {
        if (hasNecessaryPermission()) {
            initActivity();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "需要申请一些权限,取消可能导致程序不可用",
                    100,
                    NECESSARY_PERMISSION);
        }

    }

    private void initActivity() {
        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(mContext, CaptureActivity.class);
                startActivityForResult(intent, 10000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                Log.d(TAG, "onActivityResult: " + result);
            }
        }
    }

    private boolean hasNecessaryPermission() {
        return EasyPermissions.hasPermissions(this, NECESSARY_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }
}