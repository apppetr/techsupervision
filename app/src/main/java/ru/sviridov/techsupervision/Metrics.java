package ru.sviridov.techsupervision;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ru.sviridov.techsupervision.docx.DefectwithPicture;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.objects.Picture;

public abstract class Metrics {
   public static final String ADDED_VARIANT = "added variant";
   public static final String ADD_GEOMETRY = "add geometry";
   public static final String CHANGED_DEFECT = "changed defect";
   public static final String CREATED_NEW_DOCUMENT = "created new document";
   public static final String EDIT_DEFECT = "edit defect";
   public static final String FINISH_PICTURE_EDIT = "finish picture edit";
   public static final String FINISH_PICTURE_PERMISSIONS_DENIED = "FINISH_PICTURE_PERMISSIONS_DENIED";
   public static final String OPEN_COMMENTS = "open comments";
   public static final String OPEN_DEFECTS_LIST = "open defects list";
   public static final String OPEN_DOCUMENT_INFO = "open document info";
   public static final String OPEN_PICTURE_EDIT = "open picture edit";
   public static final String OPEN_SELECT_VARIANTS = "open select variants";
   public static final String PRESS_ADD_PICTURE = "presses add picture";
   public static final String SAVED_DOC = "saved document";
   public static final String SENT_DOC = "sent document";
   public static final String START_NEW_DEFECT = "start new defect";
   public static final String START_NEW_DOCUMENT = "start new document";

   private static long calcSpentTime(long var0) {
      return (System.currentTimeMillis() - var0) / 60000L;
   }

   @NonNull
   public static HashMap toMetrics(Defect var0, long var1, int var3) {
      HashMap var4 = new HashMap();
      var4.put("material", var0.getMaterial());
      var4.put("element", var0.getElement());
      String var5;
      if (var0.place == null) {
         var5 = "";
      } else {
         var5 = var0.place.toString();
      }

      var4.put("place", var5);
      var4.put("category", var0.getCategory());
      var4.put("problems", var0.getNiceProblems());
      var4.put("reasons", var0.getNiceReasons());
      var4.put("compensations", var0.getNiceCompensations());
      var4.put("volume", var0.getVolume());
      var4.put("picture count", var3);
      var4.put("spent time", calcSpentTime(var1));
      return var4;
   }

   @NonNull
   public static HashMap toMetrics(Document var0, long var1) {
      HashMap var3 = new HashMap();
      var3.put("title", var0.title);
      var3.put("responsibility", var0.responsibility.getName());
      var3.put("appointment", var0.appointment.getName());
      var3.put("year", var0.year);
      var3.put("address", var0.address);
      var3.put("sizes", var0.sizes);
      var3.put("floors", var0.floors);
      var3.put("date", new Date(var0.date));
      var3.put("spent time", calcSpentTime(var1));
      return var3;
   }

   public static HashMap toMetrics(Document var0, List var1) {
      HashMap var2 = new HashMap();
      var2.put("doc title", var0.title);
      var2.put("date", new Date(var0.date));
      var2.put("defects count", var1.size());
      int var3 = 0;
      int var4 = 0;
      Iterator var7 = var1.iterator();

      while(var7.hasNext()) {
         int var5 = ((DefectwithPicture)var7.next()).getPictures().size();
         int var6 = var3 + var5;
         var3 = var6;
         if (var4 < var5) {
            var4 = var5;
            var3 = var6;
         }
      }

      var2.put("pictures count", var3);
      var2.put("max pictures count for defect", var4);
      return var2;
   }

   @NonNull
   public static HashMap toMetrics(@NonNull Picture var0, long var1) {
      HashMap var3 = new HashMap();
      var3.put("comments count", var0.getComments().size());
      int var4;
      if (var0.getGeometries() != null) {
         var4 = var0.getGeometries().length();
      } else {
         var4 = 0;
      }

      var3.put("geometries count", var4);
      String var5;
      if (TextUtils.isEmpty(var0.getImgUrl())) {
         var5 = "empty";
      } else {
         var5 = "ok";
      }

      var3.put("image url", var5);
      return var3;
   }
}
