package ru.sviridov.techsupervision;

import com.google.gson.Gson;

public class DependencyManager {
   private static Gson gson = new Gson();

   public static Gson getGson() {
      return gson;
   }
}
