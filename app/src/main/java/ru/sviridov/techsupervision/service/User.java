package ru.sviridov.techsupervision.service;

public class User {
   private int dbVersion;
   private String token;

   public int getDbVersion() {
      return this.dbVersion;
   }

   public String getToken() {
      return this.token;
   }

   public void setDbVersion(int var1) {
      this.dbVersion = var1;
   }

   public void setToken(String var1) {
      this.token = var1;
   }
}
