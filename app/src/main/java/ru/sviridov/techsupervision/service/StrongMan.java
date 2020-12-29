package ru.sviridov.techsupervision.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import java.util.UUID;

public class StrongMan {
   public static final String GLOBAL_THING = "d0e849ad-4591-4230-8cab-08e132854e2b";
   private static final String PREFS = "some";
   private static final String USER_THING = "user_thing";
   static String userThing = null;

   private static String createUserThing(Context var0) {
      return Secure.getString(var0.getContentResolver(), "android_id") + "|" + UUID.randomUUID();
   }

   public static void init(Context var0) {
      SharedPreferences var1 = var0.getSharedPreferences("some", 0);
      userThing = var1.getString("user_thing", (String)null);
      if (userThing == null) {
         userThing = createUserThing(var0);
         var1.edit().putString("user_thing", userThing).apply();
      }

   }
}
