package ru.sviridov.techsupervision.db;

import android.content.ContentProvider;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.ArrayList;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.objects.Picture;

public class UserDataProvider extends ContentProvider {
   public static String AUTHORITY = "ru.sviridov.techsupervision.free.AUTHORITY";
   public static Uri BASE_URI;
   public static final String FRAGMENT_NO_NOTIFY = "no-notify";
   protected static final int MATCH_DEFECT_URI_CONTENT = 4130;
   protected static final int MATCH_DEFECT_URI_CONTENT_ITEM = 4129;
   protected static final int MATCH_DEFECT_WITH_PICTURE_URI_CONTENT = 4178;
   protected static final int MATCH_DOCUMENT_URI_CONTENT = 4098;
   protected static final int MATCH_DOCUMENT_URI_CONTENT_ITEM = 4113;
   protected static final int MATCH_DOCUMENT_WITH_DEFECT_URI_CONTENT = 4162;
   protected static final int MATCH_PICTURE_URL_CONTENT = 4146;
   protected static final int MATCH_PICTURE_URL_CONTENT_ITEM = 4145;
   protected static final int MATCH_TYPE_DIR = 2;
   protected static final int MATCH_TYPE_ITEM = 1;
   protected static final int MATCH_TYPE_MASK = 15;
   public static final String QUERY_GROUP_BY = "groupBy";
   public static final String QUERY_LIMIT = "limit";
   protected static final UriMatcher matcher;
   protected ContentResolver contentResolver;
   protected SQLiteOpenHelper dbHelper;

   static {
      BASE_URI = Uri.parse("content://" + AUTHORITY);
      matcher = new UriMatcher(-1);
      matcher.addURI(AUTHORITY, "Document", 4098);
      matcher.addURI(AUTHORITY, "Document/#", 4113);
      matcher.addURI(AUTHORITY, "Defect", 4130);
      matcher.addURI(AUTHORITY, "Defect/#", 4129);
      matcher.addURI(AUTHORITY, "Picture", 4146);
      matcher.addURI(AUTHORITY, "Picture/#", 4145);
      matcher.addURI(AUTHORITY, "document_with_defects", 4162);
      matcher.addURI(AUTHORITY, "defect_with_picture", 4178);
   }

   public static Uri getContentUri(String var0) {
      Uri var1;
      if (TextUtils.isEmpty(var0)) {
         var1 = null;
      } else {
         var1 = BASE_URI.buildUpon().appendPath(var0).build();
      }

      return var1;
   }

   public static Uri getContentUri(String var0, long var1) {
      Uri var3;
      if (TextUtils.isEmpty(var0)) {
         var3 = null;
      } else {
         var3 = BASE_URI.buildUpon().appendPath(var0).appendPath(String.valueOf(var1)).build();
      }

      return var3;
   }

   public static Uri getContentUri(String var0, String var1) {
      Uri var2;
      if (TextUtils.isEmpty(var0)) {
         var2 = null;
      } else {
         var2 = BASE_URI.buildUpon().appendPath(var0).appendPath(var1).build();
      }

      return var2;
   }

   public static Uri getContentUriGroupBy(String var0, String var1) {
      Uri var2;
      if (TextUtils.isEmpty(var0)) {
         var2 = null;
      } else {
         var2 = BASE_URI.buildUpon().appendPath(var0).appendQueryParameter("groupBy", var1).build();
      }

      return var2;
   }

   public static Uri getContentWithLimitUri(String var0, int var1) {
      Uri var2;
      if (TextUtils.isEmpty(var0)) {
         var2 = null;
      } else {
         var2 = BASE_URI.buildUpon().appendPath(var0).appendQueryParameter("limit", String.valueOf(var1)).build();
      }

      return var2;
   }

   public static Uri getNoNotifyContentUri(String var0) {
      Uri var1;
      if (TextUtils.isEmpty(var0)) {
         var1 = null;
      } else {
         var1 = BASE_URI.buildUpon().appendPath(var0).fragment("no-notify").build();
      }

      return var1;
   }

   public static Uri getNoNotifyContentUri(String var0, long var1) {
      Uri var3;
      if (TextUtils.isEmpty(var0)) {
         var3 = null;
      } else {
         var3 = BASE_URI.buildUpon().appendPath(var0).appendPath(String.valueOf(var1)).fragment("no-notify").build();
      }

      return var3;
   }

   protected static boolean ignoreNotify(Uri var0) {
      return "no-notify".equals(var0.getFragment());
   }

   public static void notifyUri(ContentResolver var0, Uri var1) {
      var0.notifyChange(var1, (ContentObserver)null);
      switch(matcher.match(var1)) {
      case 4129:
      case 4130:
         var0.notifyChange(getContentUri("defect_with_picture"), (ContentObserver)null);
      case 4098:
      case 4113:
         var0.notifyChange(getContentUri("document_with_defects"), (ContentObserver)null);
         break;
      case 4145:
      case 4146:
         var0.notifyChange(getContentUri("defect_with_picture"), (ContentObserver)null);
      }

   }

   @NonNull
   public ContentProviderResult[] applyBatch(@NonNull ArrayList var1) throws OperationApplicationException {
      SQLiteDatabase var2 = this.dbHelper.getWritableDatabase();
      var2.beginTransaction();

      ContentProviderResult[] var5;
      try {
         var5 = super.applyBatch(var1);
         var2.setTransactionSuccessful();
      } finally {
         var2.endTransaction();
      }

      return var5;
   }

   public int bulkInsert(Uri var1, @NonNull ContentValues[] var2) {
      String var3;
      switch(matcher.match(var1)) {
      case 4098:
         var3 = CupboardFactory.cupboard().getTable(Document.class);
         break;
      case 4130:
         var3 = CupboardFactory.cupboard().getTable(Defect.class);
         break;
      case 4146:
         var3 = CupboardFactory.cupboard().getTable(Picture.class);
         break;
      default:
         throw new IllegalArgumentException("Unsupported uri " + var1);
      }

      SQLiteDatabase var4 = this.dbHelper.getWritableDatabase();
      var4.beginTransaction();
      int var5 = 0;

      label187: {
         Throwable var10000;
         label191: {
            InsertHelper var6;
            int var7;
            boolean var10001;
            try {
               var6 = new InsertHelper(var4, var3);
               var7 = var2.length;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label191;
            }

            for(int var8 = 0; var8 < var7; ++var8) {
               try {
                  var6.replace(var2[var8]);
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label191;
               }

               ++var5;
            }

            label174:
            try {
               var6.close();
               var4.setTransactionSuccessful();
               break label187;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label174;
            }
         }

         Throwable var21 = var10000;
         var4.endTransaction();
         try {
            throw var21;
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }

      var4.endTransaction();
      if (!ignoreNotify(var1)) {
         notifyUri(this.contentResolver, var1);
      }

      return var5;
   }

   protected String composeIdSelection(String var1, String var2, String var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append(var3).append('=').append(var2);
      if (!TextUtils.isEmpty(var1)) {
         var4.append(" AND (").append(var1).append(')');
      }

      return var4.toString();
   }

   public int delete(@NonNull Uri var1, String var2, String[] var3) {
      String var4 = var2;
      String var6;
      switch(matcher.match(var1)) {
      case 4098:
         var2 = CupboardFactory.cupboard().getTable(Document.class);
         break;
      case 4113:
         var6 = CupboardFactory.cupboard().getTable(Document.class);
         var4 = this.composeIdSelection(var2, var1.getLastPathSegment(), "_id");
         var2 = var6;
         break;
      case 4129:
         var6 = CupboardFactory.cupboard().getTable(Defect.class);
         var4 = this.composeIdSelection(var2, var1.getLastPathSegment(), "_id");
         var2 = var6;
         break;
      case 4130:
         var2 = CupboardFactory.cupboard().getTable(Defect.class);
         break;
      case 4145:
         var6 = CupboardFactory.cupboard().getTable(Picture.class);
         var4 = this.composeIdSelection(var2, var1.getLastPathSegment(), "_id");
         var2 = var6;
         break;
      case 4146:
         var2 = CupboardFactory.cupboard().getTable(Picture.class);
         break;
      default:
         throw new IllegalArgumentException("Unsupported uri " + var1);
      }

      int var5 = this.dbHelper.getWritableDatabase().delete(var2, var4, var3);
      if (!ignoreNotify(var1)) {
         notifyUri(this.contentResolver, var1);
      }

      return var5;
   }

   public String getType(Uri var1) {
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
      String var3;
      switch(matcher.match(var1)) {
      case 4098:
         var3 = CupboardFactory.cupboard().getTable(Document.class);
         break;
      case 4130:
         var3 = CupboardFactory.cupboard().getTable(Defect.class);
         break;
      case 4146:
         var3 = CupboardFactory.cupboard().getTable(Picture.class);
         break;
      default:
         throw new IllegalArgumentException("Unsupported uri " + var1);
      }

      long var4 = this.dbHelper.getWritableDatabase().insertWithOnConflict(var3, (String)null, var2, 5);
      if (!ignoreNotify(var1)) {
         notifyUri(this.contentResolver, var1);
      }

      return Uri.withAppendedPath(var1, String.valueOf(var4));
   }

   public boolean onCreate() {
      Context var1 = this.getContext();
      this.dbHelper = new UserDataHelper(var1);
      this.contentResolver = var1.getContentResolver();
      return true;
   }

   @NonNull
   public Cursor query(@NonNull Uri var1, String[] var2, String var3, String[] var4, String var5) {
      SQLiteQueryBuilder var6 = new SQLiteQueryBuilder();
      Cursor var9;
      String var10;
      StringBuilder var11;
      Cursor var12;
      SQLiteDatabase var13;
      switch(matcher.match(var1)) {
      case 4098:
         var6.setTables(CupboardFactory.cupboard().getTable(Document.class));
         break;
      case 4113:
         var6.setTables(CupboardFactory.cupboard().getTable(Document.class));
         var6.appendWhere("_id = " + var1.getLastPathSegment());
         break;
      case 4129:
         var6.setTables(CupboardFactory.cupboard().getTable(Defect.class));
         var6.appendWhere("_id = " + var1.getLastPathSegment());
         break;
      case 4130:
         var6.setTables(CupboardFactory.cupboard().getTable(Defect.class));
         break;
      case 4145:
         var6.setTables(CupboardFactory.cupboard().getTable(Picture.class));
         var6.appendWhere("_id = " + var1.getLastPathSegment());
         break;
      case 4146:
         var6.setTables(CupboardFactory.cupboard().getTable(Picture.class));
         break;
      case 4162:
         var13 = this.dbHelper.getReadableDatabase();
         StringBuilder var14 = (new StringBuilder()).append("SELECT d.*, count(def.documentId) as defect_count FROM Document as d LEFT JOIN Defect as def ON d._id=def.documentId GROUP BY d._id");
         if (TextUtils.isEmpty(var3)) {
            var10 = "";
         } else {
            var10 = " where " + var3;
         }

         var11 = var14.append(var10);
         if (TextUtils.isEmpty(var5)) {
            var10 = "";
         } else {
            var10 = " order by " + var5;
         }

         var12 = var13.rawQuery(var11.append(var10).toString(), var4);
         var12.setNotificationUri(this.contentResolver, var1);
         var9 = var12;
         return var9;
      case 4178:
         String var7 = var1.getQueryParameter("groupBy");
         var13 = this.dbHelper.getReadableDatabase();
         StringBuilder var8 = (new StringBuilder()).append("SELECT d.*, p.imgUrl as imgUrl FROM Defect as d LEFT JOIN Picture as p ON d._id=p.defectId");
         if (TextUtils.isEmpty(var3)) {
            var10 = "";
         } else {
            var10 = " where " + var3;
         }

         var11 = var8.append(var10);
         if (TextUtils.isEmpty(var7)) {
            var10 = "";
         } else {
            var10 = " GROUP BY " + var7;
         }

         var11 = var11.append(var10);
         if (TextUtils.isEmpty(var5)) {
            var10 = "";
         } else {
            var10 = " order by " + var5;
         }

         var12 = var13.rawQuery(var11.append(var10).toString(), var4);
         var12.setNotificationUri(this.contentResolver, var1);
         var9 = var12;
         return var9;
      default:
         throw new IllegalArgumentException("Unsupported uri " + var1);
      }

      var12 = var6.query(this.dbHelper.getReadableDatabase(), var2, var3, var4, var1.getQueryParameter("groupBy"), (String)null, var5, var1.getQueryParameter("limit"));
      var12.setNotificationUri(this.contentResolver, var1);
      var9 = var12;
      return var9;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      String var5 = var3;
      String var7;
      switch(matcher.match(var1)) {
      case 4098:
         var3 = CupboardFactory.cupboard().getTable(Document.class);
         break;
      case 4113:
         var7 = CupboardFactory.cupboard().getTable(Document.class);
         var5 = this.composeIdSelection(var3, var1.getLastPathSegment(), "_id");
         var3 = var7;
         break;
      case 4129:
         var7 = CupboardFactory.cupboard().getTable(Defect.class);
         var5 = this.composeIdSelection(var3, var1.getLastPathSegment(), "_id");
         var3 = var7;
         break;
      case 4130:
         var3 = CupboardFactory.cupboard().getTable(Defect.class);
         break;
      case 4145:
         var7 = CupboardFactory.cupboard().getTable(Picture.class);
         var5 = this.composeIdSelection(var3, var1.getLastPathSegment(), "_id");
         var3 = var7;
         break;
      case 4146:
         var3 = CupboardFactory.cupboard().getTable(Picture.class);
         break;
      default:
         throw new IllegalArgumentException("Unsupported uri " + var1);
      }

      int var6 = this.dbHelper.getWritableDatabase().update(var3, var2, var5, var4);
      if (!ignoreNotify(var1)) {
         notifyUri(this.contentResolver, var1);
      }

      return var6;
   }
}
