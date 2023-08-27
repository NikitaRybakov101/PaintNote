package com.example.foodnote.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class Permission {

    public interface Fun {
        void onComplete(boolean result);
    }

    public static void sendPermissionForCreateFile(Fun callback, Context context, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 33) {

            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                    if(result) {
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);

                        Toast.makeText(context,"Permission to read and write files is required", Toast.LENGTH_SHORT).show();
                    }
                }).launch(Manifest.permission.READ_MEDIA_IMAGES);

            } else {
                callback.onComplete(true);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                    if(result) {
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);

                        Toast.makeText(context,"Permission to read and write files is required", Toast.LENGTH_SHORT).show();
                    }
                }).launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            } else {
                callback.onComplete(true);
            }
        }
    }
}
