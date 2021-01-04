package ru.sviridov.techsupervision;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ru.sviridov.techsupervision.docx.DefectwithPicture;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.values.Values;

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

   @NonNull
   public static HashMap<String, Object> toMetrics(Document doc, long creationTime) {
      HashMap<String, Object> attrs = new HashMap<>();
      attrs.put("title", doc.title);
      attrs.put("responsibility", doc.responsibility.getName());
      attrs.put("appointment", doc.appointment.getName());
      attrs.put("year", Integer.valueOf(doc.year));
      attrs.put("address", doc.address);
      attrs.put("sizes", doc.sizes);
      attrs.put("floors", Integer.valueOf(doc.floors));
      attrs.put(Values.Versions.DATE, new Date(doc.date));
      attrs.put("spent time", Long.valueOf(calcSpentTime(creationTime)));
      return attrs;
   }

   @NonNull
   public static HashMap<String, Object> toMetrics(Defect def, long openedTime, int pictureCount) {
      HashMap<String, Object> attrs = new HashMap<>();
      attrs.put("material", def.getMaterial());
      attrs.put("element", def.getElement());
      attrs.put("place", def.place == null ? "" : def.place.toString());
      attrs.put("category", def.getCategory());
      attrs.put("problems", def.getNiceProblems());
      attrs.put("reasons", def.getNiceReasons());
      attrs.put("compensations", def.getNiceCompensations());
      attrs.put("volume", def.getVolume());
      attrs.put("picture count", Integer.valueOf(pictureCount));
      attrs.put("spent time", Long.valueOf(calcSpentTime(openedTime)));
      return attrs;
   }

   @NonNull
   public static HashMap<String, Object> toMetrics(@NonNull Picture pict, long openedTime) {
      HashMap<String, Object> attrs = new HashMap<>();
      attrs.put("comments count", Integer.valueOf(pict.getComments().size()));
      attrs.put("geometries count", Integer.valueOf(pict.getGeometries() != null ? pict.getGeometries().length() : 0));
      attrs.put("image url", TextUtils.isEmpty(pict.getImgUrl()) ? "empty" : "ok");
      return attrs;
   }

   private static long calcSpentTime(long startTime) {
      return (System.currentTimeMillis() - startTime) / DateUtils.MILLIS_PER_MINUTE;
   }

   public static HashMap<String, Object> toMetrics(Document document, List<DefectwithPicture> defects) {
      HashMap<String, Object> attrs = new HashMap<>();
      attrs.put("doc title", document.title);
      attrs.put(Values.Versions.DATE, new Date(document.date));
      attrs.put("defects count", Integer.valueOf(defects.size()));
      int picCount = 0;
      int maxPictureCount = 0;
      for (DefectwithPicture def : defects) {
         int count = def.getPictures().size();
         picCount += count;
         if (maxPictureCount < count) {
            maxPictureCount = count;
         }
      }
      attrs.put("pictures count", Integer.valueOf(picCount));
      attrs.put("max pictures count for defect", Integer.valueOf(maxPictureCount));
      return attrs;
   }
}
