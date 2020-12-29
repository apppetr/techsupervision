package ru.sviridov.techsupervision.pictures;

public interface RequestPermissionCallback {
   void onPermissionDenied(int var1);

   void onPermissionGranted(int var1);
}
