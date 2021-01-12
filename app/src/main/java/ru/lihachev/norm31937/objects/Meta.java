package ru.lihachev.norm31937.objects;

import nl.qbusict.cupboard.annotation.Column;

public class Meta {
   @Column("manually_added")
   private volatile boolean manuallyAdded = true;
   private volatile boolean uploaded = false;
   private volatile String version;

   public String getVersion() {
      return this.version;
   }

   public boolean isManuallyAdded() {
      return this.manuallyAdded;
   }

   public boolean isUploaded() {
      return this.uploaded;
   }

   public void setIsManuallyAdded(boolean var1) {
      this.manuallyAdded = var1;
   }

   public void setIsUploaded(boolean var1) {
      this.uploaded = var1;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }
}
