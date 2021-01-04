package ru.sviridov.techsupervision;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Helper {
   public static final boolean isFree() {
      return BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR);
   }

   public static void openApp(Context context) {
      try {
         context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=ru.sviridov.techsupervision.full")));
      } catch (ActivityNotFoundException e) {
         context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=ru.sviridov.techsupervision.full")));
      }
   }
}
