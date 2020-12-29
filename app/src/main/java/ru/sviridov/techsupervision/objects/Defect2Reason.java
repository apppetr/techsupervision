package ru.sviridov.techsupervision.objects;

public class Defect2Reason extends Meta {
   private int defectId;
   private int reasonId;

   public int getDefectId() {
      return this.defectId;
   }

   public int getReasonId() {
      return this.reasonId;
   }

   public void setDefectId(int var1) {
      this.defectId = var1;
   }

   public void setReasonId(int var1) {
      this.reasonId = var1;
   }

   public String toString() {
      return "Defect2Reason{defectId=" + this.defectId + ", reasonId=" + this.reasonId + '}';
   }
}
