package ru.sviridov.techsupervision.objects;

public class Elem2Mat extends Meta {
   private int elementId;
   private int matElemId;
   private int materialId;

   public int getElementId() {
      return this.elementId;
   }

   public int getMatElemId() {
      return this.matElemId;
   }

   public int getMaterialId() {
      return this.materialId;
   }

   public void setElementId(int var1) {
      this.elementId = var1;
   }

   public void setMatElemId(int var1) {
      this.matElemId = var1;
   }

   public void setMaterialId(int var1) {
      this.materialId = var1;
   }

   public String toString() {
      return "Elem2Mat{matElemId=" + this.matElemId + ", materialId=" + this.materialId + ", elementId=" + this.elementId + '}';
   }
}
