package ru.sviridov.techsupervision.utils;

import java.util.Iterator;
import org.json.JSONArray;

public class JSONUtils {
   public static <X> Iterable<X> iterate(final JSONArray what) {
      return new Iterable<X>() {
         public Iterator<X> iterator() {
            return new Iterator<X>() {
               int index = -1;

               public boolean hasNext() {
                  return this.index + 1 < what.length();
               }

               public X next() {
                  JSONArray jSONArray = what;
                  int i = this.index + 1;
                  this.index = i;
                  return (X) jSONArray.opt(i);
               }

               public void remove() {
               }
            };
         }
      };
   }

   public static <X> Iterable<X> reiterate(final JSONArray what) {
      return new Iterable<X>() {
         public Iterator<X> iterator() {
            return new Iterator<X>() {
               int index = what.length();

               public boolean hasNext() {
                  return this.index > 0;
               }

               public X next() {
                  JSONArray jSONArray = what;
                  int i = this.index - 1;
                  this.index = i;
                  return (X) jSONArray.opt(i);
               }

               public void remove() {
               }
            };
         }
      };
   }

   public static int[] toIntArray(Object arr) {
      if (arr == null) {
         return new int[0];
      }
      if (arr instanceof Integer) {
         return new int[]{((Integer) arr).intValue()};
      }
      JSONArray array = (JSONArray) arr;
      int[] out = new int[array.length()];
      for (int i = 0; i < array.length(); i++) {
         out[i] = array.optInt(i, -1);
      }
      return out;
   }

   public static JSONArray toJSONArray(int[] array) {
      JSONArray out = new JSONArray();
      if (array != null) {
         for (int i : array) {
            out.put(i);
         }
      }
      return out;
   }
}
