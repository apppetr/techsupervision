package ru.sviridov.techsupervision.utils;

import java.util.Iterator;
import org.json.JSONArray;

public class JSONUtils {
   public static Iterable iterate(final JSONArray var0) {
      return new Iterable() {
         public Iterator iterator() {
            return new Iterator() {
               int index = -1;

               public boolean hasNext() {
                  boolean var1;
                  if (this.index + 1 < var0.length()) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  return var1;
               }

               public Object next() {
                  JSONArray var1 = var0;
                  int var2 = this.index + 1;
                  this.index = var2;
                  return var1.opt(var2);
               }

               public void remove() {
               }
            };
         }
      };
   }

   public static Iterable reiterate(final JSONArray var0) {
      return new Iterable() {
         public Iterator iterator() {
            return new Iterator() {
               int index = var0.length();

               public boolean hasNext() {
                  boolean var1;
                  if (this.index > 0) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  return var1;
               }

               public Object next() {
                  JSONArray var1 = var0;
                  int var2 = this.index - 1;
                  this.index = var2;
                  return var1.opt(var2);
               }

               public void remove() {
               }
            };
         }
      };
   }

   public static int[] toIntArray(Object var0) {
      int[] var4;
      if (var0 == null) {
         var4 = new int[0];
      } else {
         int[] var1;
         if (var0 instanceof Integer) {
            var1 = new int[]{(Integer)var0};
            var4 = var1;
         } else {
            JSONArray var2 = (JSONArray)var0;
            var1 = new int[var2.length()];
            int var3 = 0;

            while(true) {
               var4 = var1;
               if (var3 >= var2.length()) {
                  break;
               }

               var1[var3] = var2.optInt(var3, -1);
               ++var3;
            }
         }
      }

      return var4;
   }

   public static JSONArray toJSONArray(int[] var0) {
      JSONArray var1 = new JSONArray();
      if (var0 != null) {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1.put(var0[var3]);
         }
      }

      return var1;
   }
}
