package ru.sviridov.techsupervision.objects;

public class Reason2Compensation extends Meta {
   private int compensationId;
   private int reasonId;

   public int getCompensationId() {
      return this.compensationId;
   }

   public int getReasonId() {
      return this.reasonId;
   }

   public void setCompensationId(int var1) {
      this.compensationId = var1;
   }

   public void setReasonId(int var1) {
      this.reasonId = var1;
   }

   public String toString() {
      return "Reason2Compensation{reasonId=" + this.reasonId + ", compensationId=" + this.compensationId + '}';
   }
}
