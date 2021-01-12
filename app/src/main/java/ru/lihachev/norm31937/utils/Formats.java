package ru.lihachev.norm31937.utils;

import android.os.Parcelable;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import ru.lihachev.norm31937.objects.Variant;
public class Formats {
   public static String formatValues(JSONArray jsValues) throws JSONException {
      StringBuilder builder = new StringBuilder();
      int count = jsValues.length();
      for (int i = 0; i < count; i++) {
         builder.append(jsValues.getJSONObject(i).getString("name"));
         builder.append(", ");
      }
      int ind = builder.lastIndexOf(",");
      if (ind != -1) {
         builder.deleteCharAt(ind);
      }
      return builder.toString();
   }

   public static int[] extractIds(Variant[] properties) {
      if (properties == null) {
         return new int[0];
      }
      int[] result = new int[properties.length];
      Variant[] arr$ = properties;
      int len$ = arr$.length;
      int i$ = 0;
      int i = 0;
      while (i$ < len$) {
         result[i] = arr$[i$].getId();
         i$++;
         i++;
      }
      return result;
   }

   public static Variant[] migrateArray(Parcelable[] values) {
      if (values == null) {
         return new Variant[0];
      }
      Variant[] ret = new Variant[values.length];
      Parcelable[] arr$ = values;
      int len$ = arr$.length;
      int i$ = 0;
      int i = 0;
      while (i$ < len$) {
         ret[i] = (Variant) arr$[i$];
         i$++;
         i++;
      }
      return ret;
   }

   public static <T> String formatArray(T[] values) {
      if (values == null) {
         return "";
      }
      return Arrays.toString(values).replaceAll("[\\[\\]]", "");
   }

   public static String formatArray(int[] values) {
      if (values == null) {
         return "";
      }
      return Arrays.toString(values).replaceAll("[\\[\\]]", "");
   }
}
