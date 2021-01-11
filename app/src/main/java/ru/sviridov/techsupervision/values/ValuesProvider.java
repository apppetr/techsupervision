package ru.sviridov.techsupervision.values;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
public class ValuesProvider extends ContentProvider {
   public static String AUTHORITY = "ru.sviridov.techsupervision.free.PROPERTIES";
   static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
   protected static final int MATCH_TYPE_DIR = 2;
   protected static final int MATCH_TYPE_ITEM = 1;
   protected static final int MATCH_TYPE_MASK = 15;
   private static final int VALUES_COMPENSATIONS_URI_CONTENT = 8226;
   private static final int VALUES_COMPENSATIONS_URI_FORSELECTION_CONTENT = 12322;
   private static final int VALUES_D2E_URI_CONTENT = 8274;
   private static final int VALUES_D2R_URI_CONTENT = 8306;
   private static final int VALUES_DEFECTS_URI_CONTENT = 8194;
   private static final int VALUES_DEFECTS_URI_FORSELECTION_CONTENT = 12290;
   private static final int VALUES_E2M_URI_CONTENT = 8290;
   private static final int VALUES_ELEMENT_URI_CONTENT = 8242;
   private static final int VALUES_MATERIAL_URI_CONTENT = 8258;
   private static final int VALUES_MATERIAL_URI_FORSELECTION_CONTENT = 12338;
   private static final int VALUES_R2C_URI_CONTENT = 8322;
   private static final int VALUES_REASONS_URI_CONTENT = 8210;
   private static final int VALUES_REASONS_URI_FORSELECTION_CONTENT = 12306;
   protected static final UriMatcher matcher = new UriMatcher(-1);
   private ContentResolver contentResolver;
   private ValuesDataHelper dbHelper;

   static {
      matcher.addURI(AUTHORITY, "elements", VALUES_ELEMENT_URI_CONTENT);
      matcher.addURI(AUTHORITY, "materials", VALUES_MATERIAL_URI_CONTENT);
      matcher.addURI(AUTHORITY, Values.Materials.URI_FOR_SELECTION, VALUES_MATERIAL_URI_FORSELECTION_CONTENT);
      matcher.addURI(AUTHORITY, "defects", VALUES_DEFECTS_URI_CONTENT);
      matcher.addURI(AUTHORITY, Values.Defects.URI_FOR_SELECTION, VALUES_DEFECTS_URI_FORSELECTION_CONTENT);
      matcher.addURI(AUTHORITY, "reasons", VALUES_REASONS_URI_CONTENT);
      matcher.addURI(AUTHORITY, Values.Reasons.URI_FOR_SELECTION, VALUES_REASONS_URI_FORSELECTION_CONTENT);
      matcher.addURI(AUTHORITY, "compensations", VALUES_COMPENSATIONS_URI_CONTENT);
      matcher.addURI(AUTHORITY, Values.Compensations.URI_FOR_SELECTION, VALUES_COMPENSATIONS_URI_FORSELECTION_CONTENT);
      matcher.addURI(AUTHORITY, "defects2elements", VALUES_D2E_URI_CONTENT);
      matcher.addURI(AUTHORITY, "elements2materials", VALUES_E2M_URI_CONTENT);
      matcher.addURI(AUTHORITY, "defects2reasons", VALUES_D2R_URI_CONTENT);
      matcher.addURI(AUTHORITY, "reasons2compensations", VALUES_R2C_URI_CONTENT);
   }

   public static Uri uri(String uriPath) {
      return BASE_URI.buildUpon().appendPath(uriPath).build();
   }

   public static void notifyUri(ContentResolver cr, Uri uri) {
      cr.notifyChange(uri, (ContentObserver) null);
      switch (matcher.match(uri)) {
         case 8194:
         case VALUES_D2E_URI_CONTENT /*8274*/:
            cr.notifyChange(uri("defects"), (ContentObserver) null);
            return;
         default:
            return;
      }
   }

   public boolean onCreate() {
      ValuesDataHelper.niceFileJob(getContext());
      this.contentResolver = getContext().getContentResolver();
      this.dbHelper = new ValuesDataHelper(getContext());
      int version = this.dbHelper.getReadableDatabase().getVersion();
      return true;
   }

   @NonNull
   public String getType(@NonNull Uri uri) {
      switch (matcher.match(uri) & 15) {
         case 1:
            return "vnd.android.cursor.item/vnd." + AUTHORITY + ".item";
         case 2:
            return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".dir";
         default:
            throw new IllegalArgumentException("Unsupported uri " + uri);
      }
   }

   @NonNull
   public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
      String str;
      SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
      switch (matcher.match(uri)) {
         case 8194:
         case VALUES_REASONS_URI_CONTENT /*8210*/:
         case VALUES_COMPENSATIONS_URI_CONTENT /*8226*/:
         case VALUES_ELEMENT_URI_CONTENT /*8242*/:
         case VALUES_MATERIAL_URI_CONTENT /*8258*/:
         case VALUES_D2E_URI_CONTENT /*8274*/:
         case VALUES_E2M_URI_CONTENT /*8290*/:
         case VALUES_D2R_URI_CONTENT /*8306*/:
         case VALUES_R2C_URI_CONTENT /*8322*/:
            builder.setTables(uri.getLastPathSegment());
            break;
         case VALUES_DEFECTS_URI_FORSELECTION_CONTENT /*12290*/:
            builder.setTables("defects d LEFT JOIN defects2elements d2e ON (d._id = d2e.defect_id)");
            break;
         case VALUES_REASONS_URI_FORSELECTION_CONTENT /*12306*/:
            Cursor c = this.dbHelper.getReadableDatabase().rawQuery(String.format(Values.Reasons.RAW_QUERY, new Object[]{selection}) + (TextUtils.isEmpty(sortOrder) ? "" : " order by " + sortOrder), (String[]) null);
            c.setNotificationUri(this.contentResolver, uri);
            return c;
         case VALUES_COMPENSATIONS_URI_FORSELECTION_CONTENT /*12322*/:
            SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
            StringBuilder append = new StringBuilder().append(String.format(Values.Compensations.RAW_QUERY, new Object[]{selection}));
            if (TextUtils.isEmpty(sortOrder)) {
               str = "";
            } else {
               str = " order by " + sortOrder;
            }
            Cursor c1 = readableDatabase.rawQuery(append.append(str).toString(), (String[]) null);
            c1.setNotificationUri(this.contentResolver, uri);
            return c1;
         case VALUES_MATERIAL_URI_FORSELECTION_CONTENT /*12338*/:
            builder.setTables("materials m LEFT JOIN elements2materials e2m ON (m._id = e2m.material_id)");
            break;
         default:
            builder.setTables(uri.getLastPathSegment());
            break;
      }
      Cursor c2 = builder.query(this.dbHelper.getReadableDatabase(), projection, selection, selectionArgs, (String) null, (String) null, sortOrder);
      c2.setNotificationUri(this.contentResolver, uri);
      return c2;
   }

   public Uri insert(@NonNull Uri uri, ContentValues values) {
      long id = this.dbHelper.getWritableDatabase().insert(uri.getLastPathSegment(), (String) null, values);
      notifyUri(this.contentResolver, uri);
      return uri.buildUpon().appendPath(String.valueOf(id)).build();
   }

   public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
      return this.dbHelper.getWritableDatabase().delete(uri.getLastPathSegment(), selection, selectionArgs);
   }

   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      return this.dbHelper.getWritableDatabase().update(uri.getLastPathSegment(), values, selection, selectionArgs);
   }
}

