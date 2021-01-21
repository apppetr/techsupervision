package ru.lihachev.norm31937.docx;

import org.json.JSONException;

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

   public String getHuinya() throws JSONException {
      String Huinya = "";
     for(int i = 0; i < this.defect.problems.length; i ++ ){
        Huinya += Huinya + " " + this.defect.problems[i].getSnipclas().getDescription() + " " +  this.defect.problems[i].getNote() + " ; ";
     }
      return Huinya;
   }

   public String getHuinyac() throws JSONException {
      String Huinyac = "";
      for(int i = 0; i < this.defect.compensations.length; i ++ ){
         Huinyac += Huinyac + " " + this.defect.compensations[i].getSnipclas().getDescription() + " " +  this.defect.compensations[i].getNote() + " ; ";
      }
      return Huinyac;
   }

   public String getHuinyar() throws JSONException {
      String Huinyar = "";
      for(int i = 0; i < this.defect.reasons.length; i ++ ){
         Huinyar += Huinyar + " " + this.defect.reasons[i].getSnipclas().getDescription() + " " +  this.defect.reasons[i].getNote() + " ; ";
      }
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
