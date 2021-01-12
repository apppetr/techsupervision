package ru.lihachev.norm31937.objects;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;
import ru.lihachev.norm31937.GreatApplication;
import ru.lihachev.norm31937.docx.ImageProvider;

public class Picture {
   private Long _id;
   private int defectId;
   private JSONArray geometries;
   private String imgUrl;

   public Picture() {
   }

   public Picture(JSONObject jsInfo) {
      this.imgUrl = jsInfo.optString("imgurl");
      this.geometries = jsInfo.optJSONArray("geom");
   }

   public String getImgUrl() {
      return this.imgUrl;
   }

   public void setImgUrl(String imgUrl2) {
      this.imgUrl = imgUrl2;
   }

   public JSONArray getGeometries() {
      return this.geometries;
   }

   public void setGeometries(JSONArray geometries2) {
      this.geometries = geometries2;
   }

   public Long getId() {
      return this._id;
   }

   public void setId(Long id) {
      this._id = id;
   }

   public int getDefectId() {
      return this.defectId;
   }

   public void setDefectId(int defectId2) {
      this.defectId = defectId2;
   }

   public List<Comment> getComments() {
      List<Comment> comments = new ArrayList<>();
      if (this.geometries != null) {
         int counter = this.geometries.length();
         for (int i = 0; i < counter; i++) {
            JSONObject jsCommnet = this.geometries.optJSONObject(i).optJSONObject("cm");
            Comment comment = new Comment();
            comment.setDate(jsCommnet.optLong("time"));
            comment.setOrder(i + 1);
            comment.setMark(jsCommnet.optString("mark"));
            comment.setDescription(jsCommnet.optString("desc"));
            comment.setType(this.geometries.optJSONObject(i).optJSONObject("path").optString("class"));
            comments.add(comment);
         }
      }
      return comments;
   }

   @FieldMetadata(description = "image for descrpition", images = {@ImageMetadata(name = "image")})
   public IImageProvider getImage() {
      IImageProvider provider = new ImageProvider(GreatApplication.getAppContext(), this.imgUrl, true);
      provider.setUseImageSize(true);
      provider.setWidth(Float.valueOf(100.0f));
      provider.setResize(true);
      return provider;
   }
}
