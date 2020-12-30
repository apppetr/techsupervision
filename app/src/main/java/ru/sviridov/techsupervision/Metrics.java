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
   public static HashMap<String, Object> toMetrics(Defect defect, long l, int n) {
      HashMap<String, Object> hashMap = new HashMap<String, Object>();
      hashMap.put("material", defect.getMaterial());
      hashMap.put("element", defect.getElement());
      String var5;
      if (defect.place == null) {
         var5 = "";
      } else {
         var5 = defect.place.toString();
      }

      hashMap.put("place", var5);
      hashMap.put("category", defect.getCategory());
      hashMap.put("problems", defect.getNiceProblems());
      hashMap.put("reasons", defect.getNiceReasons());
      hashMap.put("compensations", defect.getNiceCompensations());
      hashMap.put("volume", defect.getVolume());
      hashMap.put("picture count", n);
      hashMap.put("spent time", calcSpentTime(l));
      return hashMap;
   }

   @NonNull
   public static HashMap<String, java.io.Serializable> toMetrics(Document document, long l) {
      HashMap<String, java.io.Serializable> hashMap = new HashMap<>();
      hashMap.put("title", document.title);
      hashMap.put("responsibility", document.responsibility.getName());
      hashMap.put("appointment", document.appointment.getName());
      hashMap.put("year", document.year);
      hashMap.put("address", document.address);
      hashMap.put("sizes", document.sizes);
      hashMap.put("floors", document.floors);
      hashMap.put("date", new Date(document.date));
      hashMap.put("spent time", calcSpentTime(l));
      return hashMap;
   }

   public static HashMap<String, java.io.Serializable> toMetrics(Document object, List<DefectwithPicture> list) {
      HashMap<String, java.io.Serializable> hashMap = new HashMap<>();
      hashMap.put("doc title", object.title);
      hashMap.put("date", new Date(object.date));
      hashMap.put("defects count", list.size());
      int n = 0;
      int n2 = 0;
      Iterator<DefectwithPicture> objectiter = list.iterator();

      while(objectiter.hasNext()) {
         int n5 = (objectiter.next()).getPictures().size();
         int n6 = n + n5;
         n = n6;
         if (n2 < n5) {
            n2 = n5;
            n = n6;
         }
      }

      hashMap.put("pictures count", n);
      hashMap.put("max pictures count for defect", n2);
      return hashMap;
   }

   @NonNull
   public static HashMap<String, java.io.Serializable> toMetrics(@NonNull Picture picture, long var1) {
      HashMap<String, java.io.Serializable> hashMap = new HashMap<>();
      hashMap.put("comments count", picture.getComments().size());
      int n4;
      if (picture.getGeometries() != null) {
         n4 = picture.getGeometries().length();
      } else {
         n4 = 0;
      }

      hashMap.put("geometries count", n4);
      String s5;
      if (TextUtils.isEmpty(picture.getImgUrl())) {
         s5 = "empty";
      } else {
         s5 = "ok";
      }

      hashMap.put("image url", s5);
      return hashMap;
   }
}
