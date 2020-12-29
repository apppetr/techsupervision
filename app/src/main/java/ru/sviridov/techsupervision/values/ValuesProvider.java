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
   static final Uri BASE_URI;
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
      BASE_URI = Uri.parse("content://" + AUTHORITY);
      matcher.addURI(AUTHORITY, "elements", 8242);
      matcher.addURI(AUTHORITY, "materials", 8258);
      matcher.addURI(AUTHORITY, "select_materials", 12338);
      matcher.addURI(AUTHORITY, "defects", 8194);
      matcher.addURI(AUTHORITY, "select_defects", 12290);
      matcher.addURI(AUTHORITY, "reasons", 8210);
      matcher.addURI(AUTHORITY, "select_reasons", 12306);
      matcher.addURI(AUTHORITY, "compensations", 8226);
      matcher.addURI(AUTHORITY, "select_compensations", 12322);
      matcher.addURI(AUTHORITY, "defects2elements", 8274);
      matcher.addURI(AUTHORITY, "elements2materials", 8290);
      matcher.addURI(AUTHORITY, "defects2reasons", 8306);
      matcher.addURI(AUTHORITY, "reasons2compensations", 8322);
   }

   public static void notifyUri(ContentResolver var0, Uri var1) {
      var0.notifyChange(var1, (ContentObserver)null);
      switch(matcher.match(var1)) {
      case 8194:
      case 8274:
         var0.notifyChange(uri("defects"), (ContentObserver)null);
      default:
      }
   }

   public static Uri uri(String var0) {
      return BASE_URI.buildUpon().appendPath(var0).build();
   }

   public int delete(@NonNull Uri var1, String var2, String[] var3) {
      return this.dbHelper.getWritableDatabase().delete(var1.getLastPathSegment(), var2, var3);
   }

   @NonNull
   public String getType(@NonNull Uri var1) {
      String var2;
      switch(matcher.match(var1) & 15) {
      case 1:
         var2 = "vnd.android.cursor.item/vnd." + AUTHORITY + ".item";
         break;
      case 2:
         var2 = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".dir";
         break;
      default:
         throw new IllegalArgumentException("Unsupported uri " + var1);
      }

      return var2;
   }

   public Uri insert(@NonNull Uri var1, ContentValues var2) {
      long var3 = this.dbHelper.getWritableDatabase().insert(var1.getLastPathSegment(), (String)null, var2);
      notifyUri(this.contentResolver, var1);
      return var1.buildUpon().appendPath(String.valueOf(var3)).build();
   }

   public boolean onCreate() {
      ValuesDataHelper.niceFileJob(this.getContext());
      this.contentResolver = this.getContext().getContentResolver();
      this.dbHelper = new ValuesDataHelper(this.getContext());
      this.dbHelper.getReadableDatabase().getVersion();
      return true;
   }

   @NonNull
   public Cursor query(@NonNull Uri var1, String[] var2, String var3, String[] var4, String var5) {
      SQLiteQueryBuilder var6 = new SQLiteQueryBuilder();
      Cursor var7;
      String var8;
      StringBuilder var9;
      Cursor var10;
      SQLiteDatabase var11;
      switch(matcher.match(var1)) {
      case 8194:
      case 8210:
      case 8226:
      case 8242:
      case 8258:
      case 8274:
      case 8290:
      case 8306:
      case 8322:
         var6.setTables(var1.getLastPathSegment());
         break;
      case 12290:
         var6.setTables("defects d LEFT JOIN defects2elements d2e ON (d._id = d2e.defect_id)");
         break;
      case 12306:
         var11 = this.dbHelper.getReadableDatabase();
         var9 = (new StringBuilder()).append(String.format("SELECT  * FROM reasons r where r._id in (select d2r.reason_id from defects2reasons d2r WHERE %s)", var3));
         if (TextUtils.isEmpty(var5)) {
            var8 = "";
         } else {
            var8 = " order by " + var5;
         }

         var10 = var11.rawQuery(var9.append(var8).toString(), (String[])null);
         var10.setNotificationUri(this.contentResolver, var1);
         var7 = var10;
         return var7;
      case 12322:
         var11 = this.dbHelper.getReadableDatabase();
         var9 = (new StringBuilder()).append(String.format("SELECT  * FROM compensations c where c._id in (select r2c.compensation_id from reasons2compensations r2c WHERE %s)", var3));
         if (TextUtils.isEmpty(var5)) {
            var8 = "";
         } else {
            var8 = " order by " + var5;
         }

         var10 = var11.rawQuery(var9.append(var8).toString(), (String[])null);
         var10.setNotificationUri(this.contentResolver, var1);
         var7 = var10;
         return var7;
      case 12338:
         var6.setTables("materials m LEFT JOIN elements2materials e2m ON (m._id = e2m.material_id)");
         break;
      default:
         var6.setTables(var1.getLastPathSegment());
      }

      var10 = var6.query(this.dbHelper.getReadableDatabase(), var2, var3, var4, (String)null, (String)null, var5);
      var10.setNotificationUri(this.contentResolver, var1);
      var7 = var10;
      return var7;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      return this.dbHelper.getWritableDatabase().update(var1.getLastPathSegment(), var2, var3, var4);
   }
}
