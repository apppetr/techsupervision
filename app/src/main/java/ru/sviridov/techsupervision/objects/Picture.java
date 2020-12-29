package ru.sviridov.techsupervision.objects;

import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.sviridov.techsupervision.GreatApplication;
import ru.sviridov.techsupervision.docx.ImageProvider;

public class Picture {
   private Long _id;
   private int defectId;
   private JSONArray geometries;
   private String imgUrl;

   public Picture() {
   }

   public Picture(JSONObject var1) {
      this.imgUrl = var1.optString("imgurl");
      this.geometries = var1.optJSONArray("geom");
   }

   public List getComments() {
      ArrayList var1 = new ArrayList();
      if (this.geometries != null) {
         int var2 = 0;

         for(int var3 = this.geometries.length(); var2 < var3; ++var2) {
            JSONObject var4 = this.geometries.optJSONObject(var2).optJSONObject("cm");
            Comment var5 = new Comment();
            var5.setDate(var4.optLong("time"));
            var5.setOrder(var2 + 1);
            var5.setMark(var4.optString("mark"));
            var5.setDescription(var4.optString("desc"));
            var5.setType(this.geometries.optJSONObject(var2).optJSONObject("path").optString("class"));
            var1.add(var5);
         }
      }

      return var1;
   }

   public int getDefectId() {
      return this.defectId;
   }

   public JSONArray getGeometries() {
      return this.geometries;
   }

   public Long getId() {
      return this._id;
   }

   @FieldMetadata(
      description = "image for descrpition",
      images = {@ImageMetadata(
   name = "image"
)}
   )
   public IImageProvider getImage() {
      ImageProvider var1 = new ImageProvider(GreatApplication.getAppContext(), this.imgUrl, true);
      var1.setUseImageSize(true);
      var1.setWidth(100.0F);
      var1.setResize(true);
      return var1;
   }

   public String getImgUrl() {
      return this.imgUrl;
   }

   public void setDefectId(int var1) {
      this.defectId = var1;
   }

   public void setGeometries(JSONArray var1) {
      this.geometries = var1;
   }

   public void setId(Long var1) {
      this._id = var1;
   }

   public void setImgUrl(String var1) {
      this.imgUrl = var1;
   }
}
