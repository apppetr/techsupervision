package ru.lihachev.norm31937.values;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
public class ValuesDataHelper extends SQLiteOpenHelper {
   private static final String ADD_IS_UPLOADED = "alter table %s add column uploaded INTEGER DEFAULT 0;";
   private static final String ADD_MANUALLY_ADDED = "alter table %s add column manually_added INTEGER DEFAULT 1;";
   private static final String ADD_VERSION = "alter table %s add column version INTEGER;";
   private static final String DB_NAME = "defects.sqlite";
   public static final int DB_VERSION = 2;
   private static final String SET_MANUALLY_ADDED = "UPDATE %s SET manually_added=1, uploaded=0 WHERE %s>%d";
   private static final String SET_NOT_MANUALLY_ADDED = "UPDATE %s SET manually_added=0 WHERE %s<=%d";
   private static final String SET_NOT_MANUALLY_ADDED2 = "UPDATE %s SET manually_added=0 WHERE %s<=%d AND %s<=%d";

   public ValuesDataHelper(Context context) {
      super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 2);
   }

   public static void niceFileJob(Context context) {
      File oldDBFile = new File(context.getFilesDir(), DB_NAME);
      File dbFile = context.getDatabasePath(DB_NAME);
      if (oldDBFile.exists()) {
         copy(oldDBFile, dbFile);
         oldDBFile.delete();
      } else if (!dbFile.exists()) {
         copyDbFromAsset(context, dbFile);
      }
   }

   private static void copy(File src, File dst) {
      try {
         FileInputStream inStream = new FileInputStream(src);
         FileOutputStream outStream = new FileOutputStream(dst);
         FileChannel inChannel = inStream.getChannel();
         inChannel.transferTo(0, inChannel.size(), outStream.getChannel());
         inStream.close();
         outStream.close();
      } catch (IOException e) {
        // Mint.logException(e);
      }
   }

   private static void copyDbFromAsset(Context context, File dst) {
      try {
         if (!dst.exists()) {
            dst.getParentFile().mkdirs();
         }
         InputStream db_stream = context.getAssets().open(DB_NAME);
         OutputStream target = new FileOutputStream(dst);
         byte[] buffer = new byte[1024];
         while (true) {
            int size = db_stream.read(buffer);
            if (size > 0) {
               target.write(buffer, 0, size);
            } else {
               target.close();
               db_stream.close();
               return;
            }
         }
      } catch (IOException e) {
        // Mint.logException(e);
      }
   }

   public void onConfigure(SQLiteDatabase db) {
      if (db.getVersion() == 0) {
         db.setVersion(1);
      }
   }

   public void onCreate(SQLiteDatabase db) {
   }

   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      if (oldVersion < 2) {
         db.execSQL(String.format(ADD_MANUALLY_ADDED, new Object[]{"elements2materials"}));
         db.execSQL(String.format(ADD_MANUALLY_ADDED, new Object[]{"defects2elements"}));
         db.execSQL(String.format(ADD_MANUALLY_ADDED, new Object[]{"defects2reasons"}));
         db.execSQL(String.format(ADD_MANUALLY_ADDED, new Object[]{"reasons2compensations"}));
         db.execSQL(String.format(ADD_MANUALLY_ADDED, new Object[]{"defects2compensations"}));
         db.execSQL(String.format(ADD_VERSION, new Object[]{"materials"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"elements"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"materials"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"elements2materials"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"defects2elements"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"defects"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"defects2reasons"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"reasons"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"reasons2compensations"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"defects2compensations"}));
         db.execSQL(String.format(ADD_IS_UPLOADED, new Object[]{"compensations"}));
         db.execSQL(String.format(SET_MANUALLY_ADDED, new Object[]{"elements", "_id", 10}));
         db.execSQL(String.format(SET_MANUALLY_ADDED, new Object[]{"materials", "_id", 6}));
         db.execSQL(String.format(SET_NOT_MANUALLY_ADDED, new Object[]{"elements2materials", "mat_elem_id", 16}));
         db.execSQL(String.format(SET_NOT_MANUALLY_ADDED2, new Object[]{"defects2elements", "defect_id", 7, "mat_elem_id", 16}));
         db.execSQL(String.format(SET_MANUALLY_ADDED, new Object[]{"defects", "_id", 7}));
         db.execSQL(String.format(SET_NOT_MANUALLY_ADDED2, new Object[]{"defects2reasons", "defect_id", 3, "reason_id", 3}));
         db.execSQL(String.format(SET_MANUALLY_ADDED, new Object[]{"reasons", "_id", 3}));
         db.execSQL(String.format(SET_NOT_MANUALLY_ADDED2, new Object[]{"reasons2compensations", "reason_id", 3, Values.R2C.COMPENSATION_ID, 5}));
         db.execSQL(String.format(SET_NOT_MANUALLY_ADDED2, new Object[]{"defects2compensations", "reason_id", 3, Values.R2C.COMPENSATION_ID, 5}));
         db.execSQL(String.format(SET_MANUALLY_ADDED, new Object[]{"compensations", "_id", 5}));
      }
   }
}
