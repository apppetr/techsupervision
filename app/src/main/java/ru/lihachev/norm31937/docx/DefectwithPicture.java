package ru.lihachev.norm31937.docx;

import org.json.JSONException;

import java.util.List;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.utils.Formats;

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
      String comment = "";
      if (defect.problems!=null){
         for(int i = 0; i < this.defect.problems.length; i ++ ){

            comment += " " + this.defect.problems[i].getName();

            if (!this.defect.problems[i].getNote().equals("")) {

               if(this.defect.problems[i].getNoteclas().getQuality().equals("1")){
                  comment += " Размерами: " + " " + this.defect.problems[i].getNoteclas().getDefectSizeDescription();
               }

               if(this.defect.problems[i].getNoteclas().getNoteToReport().equals("1")){
                  comment += " " + this.defect.problems[i].getSnipclas().getDescription();
                  comment += " Комментарий: " + this.defect.problems[i].getNoteclas().getName();
               }
            }
         }
      }

      return comment;
   }

   public String getHuinyac() throws JSONException {
      String comment = "";
      if (defect.compensations!=null){
         for(int i = 0; i < this.defect.compensations.length; i ++ ){

            comment += " " + this.defect.compensations[i].getName();

            if (!this.defect.compensations[i].getNote().equals("")) {
               if(this.defect.compensations[i].getNoteclas().getQuality().equals("1")){
                  comment += " Размерами: " + " " + this.defect.compensations[i].getNoteclas().getDefectSizeDescription();
               }

               if(this.defect.compensations[i].getNoteclas().getNoteToReport().equals("1")){
                  comment += " " + this.defect.compensations[i].getSnipclas().getDescription();
                  comment += " Комментарий: " + this.defect.compensations[i].getNoteclas().getName();
               }
            }
         }
      }

      return comment;
   }

   public String getHuinyar() throws JSONException {
      String comment = "";
      if (defect.reasons!=null){
         for(int i = 0; i < this.defect.reasons.length; i ++ ){
            comment += " " + this.defect.reasons[i].getName();

            if (!this.defect.reasons[i].getNote().equals("")) {
               if(this.defect.reasons[i].getNoteclas().getQuality().equals("1")){
                  comment += " Размерами: " + " " + this.defect.reasons[i].getNoteclas().getDefectSizeDescription();
               }

               if(this.defect.reasons[i].getNoteclas().getNoteToReport().equals("1")){

                  comment += " " + this.defect.reasons[i].getSnipclas().getDescription();
                  comment += " Комментарий: " + this.defect.reasons[i].getNoteclas().getName();
               }
            }
         }
      }


      return comment;
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
