package ru.lihachev.norm31937.objects;

public class MaterialVariant extends Variant {
   private Integer mat_elem_id;

   public MaterialVariant() {
   }

   public MaterialVariant(int var1, String var2, Integer var3) {
      super(var1, var2);
      this.mat_elem_id = var3;
   }

   public Integer getMatElemId() {
      return this.mat_elem_id;
   }

   public void setMatElemId(Integer var1) {
      this.mat_elem_id = var1;
   }
}
