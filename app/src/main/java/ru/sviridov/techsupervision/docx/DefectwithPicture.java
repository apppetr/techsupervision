package ru.sviridov.techsupervision.docx;

import java.util.List;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.utils.Formats;

/* renamed from: ru.sviridov.techsupervision.docx.DefectwithPicture */
public class DefectwithPicture {
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
      return String.format("в осях %s", new Object[]{this.defect.place});
   }
}
