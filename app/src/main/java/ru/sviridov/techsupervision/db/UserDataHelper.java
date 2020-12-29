package ru.sviridov.techsupervision.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.support.annotation.NonNull;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.sviridov.techsupervision.defects.place.object.Place;
import ru.sviridov.techsupervision.defects.place.object.PlaceConverter;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.objects.MaterialVariant;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.objects.Variant;

public class UserDataHelper extends SQLiteOpenHelper {
   private static final String DB_NAME = "user_data.db";
   private static final int DB_VERSION = 1;
   public static final String DEFECT_URL = "Defect";
   public static final String DOCUMENT_URL = "Document";
   public static final String PICTURE_URL = "Picture";
   String CREATE_TRIGGER_DELETE_REVISION = "CREATE TRIGGER IF NOT EXISTS delete_revision  AFTER DELETE ON Document BEGIN DELETE FROM Defect WHERE documentId=OLD._id ;END";

   static {
      Cupboard var0 = (new CupboardBuilder()).registerFieldConverter(Variant.class, new VariantDataConverter()).registerFieldConverter(MaterialVariant.class, new MaterialVariantDataConverter()).registerFieldConverter(Variant[].class, new VariantsArrayDataConverter()).registerFieldConverter(JSONObject.class, new JsonObjectConverter()).registerFieldConverter(JSONArray.class, new JsonArrayConverter()).registerFieldConverter(Place.class, new PlaceConverter()).useAnnotations().build();
      CupboardFactory.setCupboard(var0);
      var0.register(Document.class);
      var0.register(Defect.class);
      var0.register(Picture.class);
      var0.register(Variant.class);
      var0.register(MaterialVariant.class);
   }

   public UserDataHelper(Context var1) {
      super(var1, "user_data.db", (CursorFactory)null, 1);
   }

   public void onCreate(@NonNull SQLiteDatabase var1) {
      CupboardFactory.cupboard().withDatabase(var1).createTables();
      var1.execSQL(this.CREATE_TRIGGER_DELETE_REVISION);
   }

   public void onUpgrade(@NonNull SQLiteDatabase var1, int var2, int var3) {
      CupboardFactory.cupboard().withDatabase(var1).upgradeTables();
   }

   public interface DEFECT_WITH_PICTURE {
      String IMG_URL = "imgUrl";
      String QUERY = "SELECT d.*, p.imgUrl as imgUrl FROM Defect as d LEFT JOIN Picture as p ON d._id=p.defectId";
      String URI = "defect_with_picture";
   }

   public interface DOCUMENT_WITH_DEFECTS {
      String DEFECT_COUNT = "defect_count";
      String QUERY = "SELECT d.*, count(def.documentId) as defect_count FROM Document as d LEFT JOIN Defect as def ON d._id=def.documentId GROUP BY d._id";
      String URI = "document_with_defects";
   }
}
