package ru.sviridov.techsupervision.pictures;

import android.app.Activity;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;

public class PermissionController {
   private final Activity activity;

   public PermissionController(@NonNull Activity var1) {
      this.activity = var1;
   }

   public boolean isPermissionGranted(@NonNull PermissionType var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 23 && (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this.activity, var1.permissionName) != 0)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, int[] var3, @NonNull RequestPermissionCallback var4) {
      if (var3.length > 0 && var3[0] == 0) {
         var4.onPermissionGranted(var1);
      } else {
         var4.onPermissionDenied(var1);
      }

   }

   public void requestGroupOfUserGrantPermission(int var1, @NonNull PermissionType... var2) {
      if (VERSION.SDK_INT >= 23 && var2 != null && var2.length != 0) {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            PermissionType var5 = var2[var4];
            if (!this.isPermissionGranted(var5)) {
               var3.add(var5.permissionName);
            }
         }

         if (var3.size() > 0) {
            ActivityCompat.requestPermissions(this.activity, (String[])var3.toArray(new String[var3.size()]), var1);
         }
      }

   }

   public void requestUserGrantPermission(@NonNull PermissionType var1) {
      if (!this.isPermissionGranted(var1)) {
         Activity var2 = this.activity;
         String var3 = var1.permissionName;
         int var4 = var1.requestCode;
         ActivityCompat.requestPermissions(var2, new String[]{var3}, var4);
      }

   }
}
