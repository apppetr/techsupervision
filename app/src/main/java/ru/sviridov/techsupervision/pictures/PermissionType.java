package ru.sviridov.techsupervision.pictures;

import android.support.annotation.NonNull;

public enum PermissionType {
   GOOGLE_PHOTOS("com.google.android.apps.photos.permission.GOOGLE_PHOTOS", 13),
   WRITE_EXTERNAL_STORAGE("android.permission.WRITE_EXTERNAL_STORAGE", 12);

   public final String permissionName;
   public final Integer requestCode;

   private PermissionType(@NonNull String var3, Integer var4) {
      this.permissionName = var3;
      this.requestCode = var4;
   }
}
