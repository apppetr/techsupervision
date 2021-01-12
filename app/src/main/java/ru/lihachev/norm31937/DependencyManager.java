package ru.lihachev.norm31937;

import com.google.gson.Gson;

public class DependencyManager {
   private static Gson gson = new Gson();

   public static Gson getGson() {
      return gson;
   }
}
