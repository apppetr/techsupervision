package ru.sviridov.techsupervision.db;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
   public static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
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
   protected static final UriMatcher matcher = new UriMatcher(-1);
   protected ContentResolver contentResolver;
   protected SQLiteOpenHelper dbHelper;

   static {
      matcher.addURI(AUTHORITY, UserDataHelper.DOCUMENT_URL, 4098);
      matcher.addURI(AUTHORITY, "Document/#", MATCH_DOCUMENT_URI_CONTENT_ITEM);
      matcher.addURI(AUTHORITY, UserDataHelper.DEFECT_URL, MATCH_DEFECT_URI_CONTENT);
      matcher.addURI(AUTHORITY, "Defect/#", MATCH_DEFECT_URI_CONTENT_ITEM);
      matcher.addURI(AUTHORITY, UserDataHelper.PICTURE_URL, MATCH_PICTURE_URL_CONTENT);
      matcher.addURI(AUTHORITY, "Picture/#", MATCH_PICTURE_URL_CONTENT_ITEM);
      matcher.addURI(AUTHORITY, UserDataHelper.DOCUMENT_WITH_DEFECTS.URI, MATCH_DOCUMENT_WITH_DEFECT_URI_CONTENT);
      matcher.addURI(AUTHORITY, UserDataHelper.DEFECT_WITH_PICTURE.URI, MATCH_DEFECT_WITH_PICTURE_URI_CONTENT);
   }

   public boolean onCreate() {
      Context context = getContext();
      this.dbHelper = new UserDataHelper(context);
      this.contentResolver = context.getContentResolver();
      return true;
   }

   public String getType(Uri uri) {
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
      String str2;
      String str3;
      SQLiteQueryBuilder query = new SQLiteQueryBuilder();
      switch (matcher.match(uri)) {
         case 4098:
            query.setTables(CupboardFactory.cupboard().getTable(Document.class));
            break;
         case MATCH_DOCUMENT_URI_CONTENT_ITEM /*4113*/:
            query.setTables(CupboardFactory.cupboard().getTable(Document.class));
            query.appendWhere("_id = " + uri.getLastPathSegment());
            break;
         case MATCH_DEFECT_URI_CONTENT_ITEM /*4129*/:
            query.setTables(CupboardFactory.cupboard().getTable(Defect.class));
            query.appendWhere("_id = " + uri.getLastPathSegment());
            break;
         case MATCH_DEFECT_URI_CONTENT /*4130*/:
            query.setTables(CupboardFactory.cupboard().getTable(Defect.class));
            break;
         case MATCH_PICTURE_URL_CONTENT_ITEM /*4145*/:
            query.setTables(CupboardFactory.cupboard().getTable(Picture.class));
            query.appendWhere("_id = " + uri.getLastPathSegment());
            break;
         case MATCH_PICTURE_URL_CONTENT /*4146*/:
            query.setTables(CupboardFactory.cupboard().getTable(Picture.class));
            break;
         case MATCH_DOCUMENT_WITH_DEFECT_URI_CONTENT /*4162*/:
            Cursor c = this.dbHelper.getReadableDatabase().rawQuery(UserDataHelper.DOCUMENT_WITH_DEFECTS.QUERY + (TextUtils.isEmpty(selection) ? "" : " where " + selection) + (TextUtils.isEmpty(sortOrder) ? "" : " order by " + sortOrder), selectionArgs);
            c.setNotificationUri(this.contentResolver, uri);
            return c;
         case MATCH_DEFECT_WITH_PICTURE_URI_CONTENT /*4178*/:
            String groupBy = uri.getQueryParameter(QUERY_GROUP_BY);
            SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
            StringBuilder append = new StringBuilder().append(UserDataHelper.DEFECT_WITH_PICTURE.QUERY);
            if (TextUtils.isEmpty(selection)) {
               str = "";
            } else {
               str = " where " + selection;
            }
            StringBuilder append2 = append.append(str);
            if (TextUtils.isEmpty(groupBy)) {
               str2 = "";
            } else {
               str2 = " GROUP BY " + groupBy;
            }
            StringBuilder append3 = append2.append(str2);
            if (TextUtils.isEmpty(sortOrder)) {
               str3 = "";
            } else {
               str3 = " order by " + sortOrder;
            }
            Cursor c2 = readableDatabase.rawQuery(append3.append(str3).toString(), selectionArgs);
            c2.setNotificationUri(this.contentResolver, uri);
            return c2;
         default:
            throw new IllegalArgumentException("Unsupported uri " + uri);
      }
      Cursor c3 = query.query(this.dbHelper.getReadableDatabase(), projection, selection, selectionArgs, uri.getQueryParameter(QUERY_GROUP_BY), (String) null, sortOrder, uri.getQueryParameter(QUERY_LIMIT));
      c3.setNotificationUri(this.contentResolver, uri);
      return c3;
   }

   @NonNull
   public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
      SQLiteDatabase sql = this.dbHelper.getWritableDatabase();
      sql.beginTransaction();
      try {
         ContentProviderResult[] res = super.applyBatch(operations);
         sql.setTransactionSuccessful();
         return res;
      } finally {
         sql.endTransaction();
      }
   }

   /* JADX INFO: finally extract failed */
   public int bulkInsert(Uri uri, @NonNull ContentValues[] valuesAr) {
      String table;
      switch (matcher.match(uri)) {
         case 4098:
            table = CupboardFactory.cupboard().getTable(Document.class);
            break;
         case MATCH_DEFECT_URI_CONTENT /*4130*/:
            table = CupboardFactory.cupboard().getTable(Defect.class);
            break;
         case MATCH_PICTURE_URL_CONTENT /*4146*/:
            table = CupboardFactory.cupboard().getTable(Picture.class);
            break;
         default:
            throw new IllegalArgumentException("Unsupported uri " + uri);
      }
      SQLiteDatabase sql = this.dbHelper.getWritableDatabase();
      sql.beginTransaction();
      int count = 0;
      try {
         DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(sql, table);
         for (ContentValues values : valuesAr) {
            ih.replace(values);
            count++;
         }
         ih.close();
         sql.setTransactionSuccessful();
         sql.endTransaction();
         if (!ignoreNotify(uri)) {
            notifyUri(this.contentResolver, uri);
         }
         return count;
      } catch (Throwable th) {
         sql.endTransaction();
         throw th;
      }
   }

   public Uri insert(@NonNull Uri uri, ContentValues values) {
      String table;
      switch (matcher.match(uri)) {
         case 4098:
            table = CupboardFactory.cupboard().getTable(Document.class);
            break;
         case MATCH_DEFECT_URI_CONTENT /*4130*/:
            table = CupboardFactory.cupboard().getTable(Defect.class);
            break;
         case MATCH_PICTURE_URL_CONTENT /*4146*/:
            table = CupboardFactory.cupboard().getTable(Picture.class);
            break;
         default:
            throw new IllegalArgumentException("Unsupported uri " + uri);
      }
      long id = this.dbHelper.getWritableDatabase().insertWithOnConflict(table, (String) null, values, 5);
      if (!ignoreNotify(uri)) {
         notifyUri(this.contentResolver, uri);
      }
      return Uri.withAppendedPath(uri, String.valueOf(id));
   }

   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      String table;
      String processedSelection = selection;
      switch (matcher.match(uri)) {
         case 4098:
            table = CupboardFactory.cupboard().getTable(Document.class);
            break;
         case MATCH_DOCUMENT_URI_CONTENT_ITEM /*4113*/:
            table = CupboardFactory.cupboard().getTable(Document.class);
            processedSelection = composeIdSelection(selection, uri.getLastPathSegment(), "_id");
            break;
         case MATCH_DEFECT_URI_CONTENT_ITEM /*4129*/:
            table = CupboardFactory.cupboard().getTable(Defect.class);
            processedSelection = composeIdSelection(selection, uri.getLastPathSegment(), "_id");
            break;
         case MATCH_DEFECT_URI_CONTENT /*4130*/:
            table = CupboardFactory.cupboard().getTable(Defect.class);
            break;
         case MATCH_PICTURE_URL_CONTENT_ITEM /*4145*/:
            table = CupboardFactory.cupboard().getTable(Picture.class);
            processedSelection = composeIdSelection(selection, uri.getLastPathSegment(), "_id");
            break;
         case MATCH_PICTURE_URL_CONTENT /*4146*/:
            table = CupboardFactory.cupboard().getTable(Picture.class);
            break;
         default:
            throw new IllegalArgumentException("Unsupported uri " + uri);
      }
      int count = this.dbHelper.getWritableDatabase().update(table, values, processedSelection, selectionArgs);
      if (!ignoreNotify(uri)) {
         notifyUri(this.contentResolver, uri);
      }
      return count;
   }

   public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
      String table;
      String processedSelection = selection;
      switch (matcher.match(uri)) {
         case 4098:
            table = CupboardFactory.cupboard().getTable(Document.class);
            break;
         case MATCH_DOCUMENT_URI_CONTENT_ITEM /*4113*/:
            table = CupboardFactory.cupboard().getTable(Document.class);
            processedSelection = composeIdSelection(selection, uri.getLastPathSegment(), "_id");
            break;
         case MATCH_DEFECT_URI_CONTENT_ITEM /*4129*/:
            table = CupboardFactory.cupboard().getTable(Defect.class);
            processedSelection = composeIdSelection(selection, uri.getLastPathSegment(), "_id");
            break;
         case MATCH_DEFECT_URI_CONTENT /*4130*/:
            table = CupboardFactory.cupboard().getTable(Defect.class);
            break;
         case MATCH_PICTURE_URL_CONTENT_ITEM /*4145*/:
            table = CupboardFactory.cupboard().getTable(Picture.class);
            processedSelection = composeIdSelection(selection, uri.getLastPathSegment(), "_id");
            break;
         case MATCH_PICTURE_URL_CONTENT /*4146*/:
            table = CupboardFactory.cupboard().getTable(Picture.class);
            break;
         default:
            throw new IllegalArgumentException("Unsupported uri " + uri);
      }
      int count = this.dbHelper.getWritableDatabase().delete(table, processedSelection, selectionArgs);
      if (!ignoreNotify(uri)) {
         notifyUri(this.contentResolver, uri);
      }
      return count;
   }

   public static void notifyUri(ContentResolver cr, Uri uri) {
      cr.notifyChange(uri, (ContentObserver) null);
      switch (matcher.match(uri)) {
         case 4098:
         case MATCH_DOCUMENT_URI_CONTENT_ITEM /*4113*/:
            break;
         case MATCH_DEFECT_URI_CONTENT_ITEM /*4129*/:
         case MATCH_DEFECT_URI_CONTENT /*4130*/:
            cr.notifyChange(getContentUri(UserDataHelper.DEFECT_WITH_PICTURE.URI), (ContentObserver) null);
            break;
         case MATCH_PICTURE_URL_CONTENT_ITEM /*4145*/:
         case MATCH_PICTURE_URL_CONTENT /*4146*/:
            cr.notifyChange(getContentUri(UserDataHelper.DEFECT_WITH_PICTURE.URI), (ContentObserver) null);
            return;
         default:
            return;
      }
      cr.notifyChange(getContentUri(UserDataHelper.DOCUMENT_WITH_DEFECTS.URI), (ContentObserver) null);
   }

   protected static boolean ignoreNotify(Uri uri) {
      return FRAGMENT_NO_NOTIFY.equals(uri.getFragment());
   }

   /* access modifiers changed from: protected */
   public String composeIdSelection(String originalSelection, String id, String idColumn) {
      StringBuffer sb = new StringBuffer();
      sb.append(idColumn).append('=').append(id);
      if (!TextUtils.isEmpty(originalSelection)) {
         sb.append(" AND (").append(originalSelection).append(')');
      }
      return sb.toString();
   }

   public static Uri getContentUri(String path) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).build();
   }

   public static Uri getContentUriGroupBy(String path, String groupBy) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).appendQueryParameter(QUERY_GROUP_BY, groupBy).build();
   }

   public static Uri getContentUri(String path, long id) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).appendPath(String.valueOf(id)).build();
   }

   public static Uri getContentUri(String path, String id) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).appendPath(id).build();
   }

   public static Uri getContentWithLimitUri(String path, int limit) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).appendQueryParameter(QUERY_LIMIT, String.valueOf(limit)).build();
   }

   public static Uri getNoNotifyContentUri(String path) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).fragment(FRAGMENT_NO_NOTIFY).build();
   }

   public static Uri getNoNotifyContentUri(String path, long id) {
      if (TextUtils.isEmpty(path)) {
         return null;
      }
      return BASE_URI.buildUpon().appendPath(path).appendPath(String.valueOf(id)).fragment(FRAGMENT_NO_NOTIFY).build();
   }
}
