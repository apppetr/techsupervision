package ru.sviridov.techsupervision.docx;

import java.util.List;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.utils.Formats;

public class DefectwithPicture {
   private Defect defect;
   private int order;
   private List pictures;

   public String getCategory() {
      return this.defect.getCategory();
   }

   public String getCoordinates() {
      String var1;
      if (this.defect.place == null) {
         var1 = "";
      } else {
         var1 = String.format("в осях %s", this.defect.place);
      }

      return var1;
   }

   public Variant getElement() {
      return this.defect.getElement();
   }

   public Variant getMaterial() {
      return this.defect.getMaterial();
   }

   public String getNiceCompensations() {
      return Formats.formatArray((Object[])this.defect.compensations);
   }

   public String getNiceProblems() {
      return Formats.formatArray((Object[])this.defect.problems);
   }

   public String getNiceReasons() {
      return Formats.formatArray((Object[])this.defect.reasons);
   }

   public int getOrder() {
      return this.order;
   }

   public List getPictures() {
      return this.pictures;
   }

   public String getVolume() {
      String var1;
      if (this.defect.getVolume() == null) {
         var1 = "";
      } else {
         var1 = this.defect.getVolume();
      }

      return var1;
   }

   public void setDefect(Defect var1) {
      this.defect = var1;
   }

   public void setOrder(int var1) {
      this.order = var1;
   }

   public void setPictures(List var1) {
      this.pictures = var1;
   }
}
