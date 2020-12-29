package ru.sviridov.techsupervision;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Helper {
   public static final boolean isFree() {
      return "free".equals("free");
   }

   public static void openApp(Context var0) {
      try {
         Intent var1 = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=ru.sviridov.techsupervision.full"));
         var0.startActivity(var1);
      } catch (ActivityNotFoundException var2) {
         var0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=ru.sviridov.techsupervision.full")));
      }

   }
}
