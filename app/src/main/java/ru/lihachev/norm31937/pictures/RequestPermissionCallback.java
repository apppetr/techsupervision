package ru.lihachev.norm31937.pictures;

public interface RequestPermissionCallback {
   void onPermissionDenied(int var1);

   void onPermissionGranted(int var1);
}
