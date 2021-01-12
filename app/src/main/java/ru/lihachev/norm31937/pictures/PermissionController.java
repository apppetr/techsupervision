package ru.lihachev.norm31937.pictures;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ru.lihachev.norm31937.pictures.PermissionController */
public class PermissionController {
   private final Activity activity;

   public PermissionController(@NonNull Activity activity2) {
      this.activity = activity2;
   }

   public boolean isPermissionGranted(@NonNull PermissionType permissionType) {
      //return Build.VERSION.SDK_INT < 23 || (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.activity, permissionType.permissionName) == 0);
  return true;
   }

   public void requestUserGrantPermission(@NonNull PermissionType permissionType) {
      if (!isPermissionGranted(permissionType)) {
         ActivityCompat.requestPermissions(this.activity, new String[]{permissionType.permissionName}, permissionType.requestCode.intValue());
      }
   }

   public void requestGroupOfUserGrantPermission(int requestCode, @NonNull PermissionType... permissionType) {
      if (Build.VERSION.SDK_INT >= 23 && permissionType != null && permissionType.length != 0) {
         List<String> permissionTypeList = new ArrayList<>();
         for (PermissionType permission : permissionType) {
            if (!isPermissionGranted(permission)) {
               permissionTypeList.add(permission.permissionName);
            }
         }
         if (permissionTypeList.size() > 0) {
            ActivityCompat.requestPermissions(this.activity, (String[]) permissionTypeList.toArray(new String[permissionTypeList.size()]), requestCode);
         }
      }
   }

   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults, @NonNull RequestPermissionCallback requestPermissionCallback) {
      if (grantResults.length <= 0 || grantResults[0] != 0) {
         requestPermissionCallback.onPermissionDenied(requestCode);
      } else {
         requestPermissionCallback.onPermissionGranted(requestCode);
      }
   }
}
