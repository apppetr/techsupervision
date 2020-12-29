package ru.sviridov.techsupervision.objects;

public class Comment {
   private long date;
   private String description;
   private String mark;
   private int order;
   private String type;

   public long getDate() {
      return this.date;
   }

   public String getDescription() {
      return this.description;
   }

   public String getMark() {
      return this.mark;
   }

   public int getOrder() {
      return this.order;
   }

   public String getType() {
      return this.type;
   }

   public void setDate(long var1) {
      this.date = var1;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public void setMark(String var1) {
      this.mark = var1;
   }

   public void setOrder(int var1) {
      this.order = var1;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String toString() {
      return String.format("order=%d, type=%s, mark=%s, description=%s, date= %d", this.order, this.type, this.mark, this.description, this.date);
   }
}
