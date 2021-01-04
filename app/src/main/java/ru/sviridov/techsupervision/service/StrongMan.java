package ru.sviridov.techsupervision.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.provider.Settings.Secure;
import java.util.UUID;

public class StrongMan {
   public static final String GLOBAL_THING = "d0e849ad-4591-4230-8cab-08e132854e2b";
   private static final String PREFS = "some";
   private static final String USER_THING = "user_thing";
   static String userThing = null;

   public static void init(Context context) {
      SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
      userThing = prefs.getString(USER_THING, (String) null);
      if (userThing == null) {
         userThing = createUserThing(context);
         prefs.edit().putString(USER_THING, userThing).apply();
      }
   }

   private static String createUserThing(Context context) {
      return Settings.Secure.getString(context.getContentResolver(), "android_id") + "|" + UUID.randomUUID();
   }
}
