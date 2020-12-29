package ru.sviridov.techsupervision.utils;

import android.os.Parcelable;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import ru.sviridov.techsupervision.objects.Variant;

public class Formats {
   public static int[] extractIds(Variant[] var0) {
      int[] var1;
      if (var0 == null) {
         var1 = new int[0];
      } else {
         int[] var2 = new int[var0.length];
         int var3 = var0.length;
         int var4 = 0;
         int var5 = 0;

         while(true) {
            var1 = var2;
            if (var4 >= var3) {
               break;
            }

            var2[var5] = var0[var4].getId();
            ++var4;
            ++var5;
         }
      }

      return var1;
   }

   public static String formatArray(int[] var0) {
      String var1;
      if (var0 == null) {
         var1 = "";
      } else {
         var1 = Arrays.toString(var0).replaceAll("[\\[\\]]", "");
      }

      return var1;
   }

   public static String formatArray(Object[] var0) {
      String var1;
      if (var0 == null) {
         var1 = "";
      } else {
         var1 = Arrays.toString(var0).replaceAll("[\\[\\]]", "");
      }

      return var1;
   }

   public static String formatValues(JSONArray var0) throws JSONException {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         var1.append(var0.getJSONObject(var2).getString("name"));
         var1.append(", ");
      }

      var2 = var1.lastIndexOf(",");
      if (var2 != -1) {
         var1.deleteCharAt(var2);
      }

      return var1.toString();
   }

   public static Variant[] migrateArray(Parcelable[] var0) {
      Variant[] var1;
      if (var0 == null) {
         var1 = new Variant[0];
      } else {
         Variant[] var2 = new Variant[var0.length];
         int var3 = var0.length;
         int var4 = 0;
         int var5 = 0;

         while(true) {
            var1 = var2;
            if (var4 >= var3) {
               break;
            }

            var2[var5] = (Variant)var0[var4];
            ++var4;
            ++var5;
         }
      }

      return var1;
   }
}
