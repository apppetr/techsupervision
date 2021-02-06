package ru.lihachev.norm31937.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.lihachev.norm31937.defects.place.object.Place;
import ru.lihachev.norm31937.defects.place.object.PlaceConverter;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Document;
import ru.lihachev.norm31937.objects.MaterialVariant;
import ru.lihachev.norm31937.objects.Organization;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.objects.Variant;

public class UserDataHelper extends SQLiteOpenHelper {
   private static final String DB_NAME = "user_data.db";
   private static final int DB_VERSION = 1;
   public static final String DEFECT_URL = "Defect";
   public static final String DOCUMENT_URL = "Document";
   public static final String ORGANIZATION_URL = "Organization";
   public static final String PICTURE_URL = "Picture";
   String CREATE_TRIGGER_DELETE_REVISION = "CREATE TRIGGER IF NOT EXISTS delete_revision  AFTER DELETE ON Document BEGIN DELETE FROM Defect WHERE documentId=OLD._id ;END";

   public interface DEFECT_WITH_PICTURE {
      public static final String IMG_URL = "imgUrl";
      public static final String QUERY = "SELECT d.*, p.imgUrl as imgUrl FROM Defect as d LEFT JOIN Picture as p ON d._id=p.defectId";
      public static final String URI = "defect_with_picture";
   }

   public interface DOCUMENT_WITH_DEFECTS {
      public static final String DEFECT_COUNT = "defect_count";
      public static final String QUERY = "SELECT d.*, count(def.documentId) as defect_count FROM Document as d LEFT JOIN Defect as def ON d._id=def.documentId GROUP BY d._id";
      public static final String URI = "document_with_defects";
   }

   public interface ORGANIZATIONS_LIST {
      public static final String ORGANIZATIONS_COUNT = "organiztions_count";
      public static final String QUERY = "SELECT d.*, count(def.documentId) as defect_count FROM Document as d LEFT JOIN Defect as def ON d._id=def.documentId GROUP BY d._id";
      public static final String URI = "organizations_list";
   }

   static {
      Cupboard cb = new CupboardBuilder().registerFieldConverter(Variant.class, new VariantDataConverter()).registerFieldConverter(MaterialVariant.class, new MaterialVariantDataConverter()).registerFieldConverter(Variant[].class, new VariantsArrayDataConverter()).registerFieldConverter(JSONObject.class, new JsonObjectConverter()).registerFieldConverter(JSONArray.class, new JsonArrayConverter()).registerFieldConverter(Place.class, new PlaceConverter()).useAnnotations().build();
      CupboardFactory.setCupboard(cb);
      cb.register(Document.class);
      cb.register(Defect.class);
      cb.register(Picture.class);
      cb.register(Variant.class);
      cb.register(MaterialVariant.class);
      cb.register(Organization.class);
   }

   public UserDataHelper(Context context) {
      super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
   }

   public void onCreate(@NonNull SQLiteDatabase db) {
      CupboardFactory.cupboard().withDatabase(db).createTables();
      db.execSQL(this.CREATE_TRIGGER_DELETE_REVISION);
   }

   public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
      CupboardFactory.cupboard().withDatabase(db).upgradeTables();
   }
}
