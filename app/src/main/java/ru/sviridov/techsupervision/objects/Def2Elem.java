package ru.sviridov.techsupervision.objects;

public class Def2Elem extends Meta {
   private int defectId;
   private int matElemId;

   public int getDefectId() {
      return this.defectId;
   }

   public int getMatElemId() {
      return this.matElemId;
   }

   public void setDefectId(int var1) {
      this.defectId = var1;
   }

   public void setMatElemId(int var1) {
      this.matElemId = var1;
   }

   public String toString() {
      return "Def2Elem{defectId=" + this.defectId + ", matElemId=" + this.matElemId + '}';
   }
}
