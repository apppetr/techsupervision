package ru.sviridov.techsupervision.objects;

public class Comment {
   private long date;
   private String description;
   private String mark;
   private int order;
   private String type;

   public int getOrder() {
      return this.order;
   }

   public void setOrder(int order2) {
      this.order = order2;
   }

   public String getMark() {
      return this.mark;
   }

   public void setMark(String mark2) {
      this.mark = mark2;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description2) {
      this.description = description2;
   }

   public long getDate() {
      return this.date;
   }

   public void setDate(long date2) {
      this.date = date2;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type2) {
      this.type = type2;
   }

   public String toString() {
      return String.format("order=%d, type=%s, mark=%s, description=%s, date= %d", new Object[]{Integer.valueOf(this.order), this.type, this.mark, this.description, Long.valueOf(this.date)});
   }
}