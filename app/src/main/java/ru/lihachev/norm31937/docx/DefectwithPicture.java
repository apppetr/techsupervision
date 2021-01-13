package ru.lihachev.norm31937.docx;

import java.util.List;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.utils.Formats;

/* renamed from: ru.lihachev.norm31937.docx.DefectwithPicture */
public class DefectwithPicture {
   public String Problems;
   private Defect defect;

   private int order;
   private List<Picture> pictures;

   public List<Picture> getPictures() {
      return this.pictures;
   }

   public void setPictures(List<Picture> pictures2) {
      this.pictures = pictures2;
   }

   public void setDefect(Defect defect2) {
      this.defect = defect2;
   }

   public String getVolume() {

      return this.defect.getVolume() == null ? "" : this.defect.getVolume();
   }

   public String getHuinya() {
      String Huinya = this.defect.getNiceProblems();
      return Huinya;
   }

   public String getHuinyac() {
      String Huinyac = this.defect.getNiceCompensations();
      return Huinyac;
   }

   public String getHuinyar() {
      String Huinyar = this.defect.getNiceReasons();
      return Huinyar;
   }
   public int getOrder() {
      return this.order;
   }

   public void setOrder(int order2) {
      this.order = order2;
   }

   public Variant getElement() {

      return this.defect.getElement();
   }

   public Variant getMaterial() {
      return this.defect.getMaterial();
   }

   public String getCategory() {
      return this.defect.getCategory();
   }

   public String getCoordinates() {
      if (this.defect.place == null) {
         return "";
      }
      return String.format("в осях %s", this.defect.place);
   }
}
