package ru.sviridov.techsupervision.values;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

   public ValuesDataHelper(Context var1) {
      super(var1, "defects.sqlite", (CursorFactory)null, 2);
   }

   private static void copy(File var0, File var1) {
      try {
         FileInputStream var2 = new FileInputStream(var0);
         FileOutputStream var5 = new FileOutputStream(var1);
         FileChannel var6 = var2.getChannel();
         FileChannel var3 = var5.getChannel();
         var6.transferTo(0L, var6.size(), var3);
         var2.close();
         var5.close();
      } catch (IOException var4) {
       //  Mint.logException(var4);
      }

   }

   private static void copyDbFromAsset(Context var0, File var1) {
      IOException var10000;
      label47: {
         boolean var10001;
         if (!var1.exists()) {
            var1.getParentFile().mkdirs();
         }

         FileOutputStream var2;
         InputStream var9;
         byte[] var11;
         try {
            var9 = var0.getAssets().open("defects.sqlite");
            var2 = new FileOutputStream(var1);
            var11 = new byte[1024];
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label47;
         }

         while(true) {
            int var3;
            try {
               var3 = var9.read(var11);
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break;
            }

            if (var3 <= 0) {
               try {
                  var2.close();
                  var9.close();
                  return;
               } catch (IOException var4) {
                  var10000 = var4;
                  var10001 = false;
                  break;
               }
            }

            try {
               var2.write(var11, 0, var3);
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }
         }
      }

      IOException var10 = var10000;
      //Mint.logException(var10);
   }

   public static void niceFileJob(Context var0) {
      File var1 = new File(var0.getFilesDir(), "defects.sqlite");
      File var2 = var0.getDatabasePath("defects.sqlite");
      if (var1.exists()) {
         copy(var1, var2);
         var1.delete();
      } else if (!var2.exists()) {
         copyDbFromAsset(var0, var2);
      }

   }

   public void onConfigure(SQLiteDatabase var1) {
      if (var1.getVersion() == 0) {
         var1.setVersion(1);
      }

   }

   public void onCreate(SQLiteDatabase var1) {
   }

   public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
      if (var2 < 2) {
         var1.execSQL(String.format("alter table %s add column manually_added INTEGER DEFAULT 1;", "elements2materials"));
         var1.execSQL(String.format("alter table %s add column manually_added INTEGER DEFAULT 1;", "defects2elements"));
         var1.execSQL(String.format("alter table %s add column manually_added INTEGER DEFAULT 1;", "defects2reasons"));
         var1.execSQL(String.format("alter table %s add column manually_added INTEGER DEFAULT 1;", "reasons2compensations"));
         var1.execSQL(String.format("alter table %s add column version INTEGER;", "materials"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "elements"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "materials"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "elements2materials"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "defects2elements"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "defects"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "defects2reasons"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "reasons"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "reasons2compensations"));
         var1.execSQL(String.format("alter table %s add column uploaded INTEGER DEFAULT 0;", "compensations"));
         var1.execSQL(String.format("UPDATE %s SET manually_added=1, uploaded=0 WHERE %s>%d", "elements", "_id", 10));
         var1.execSQL(String.format("UPDATE %s SET manually_added=1, uploaded=0 WHERE %s>%d", "materials", "_id", 6));
         var1.execSQL(String.format("UPDATE %s SET manually_added=0 WHERE %s<=%d", "elements2materials", "mat_elem_id", 16));
         var1.execSQL(String.format("UPDATE %s SET manually_added=0 WHERE %s<=%d AND %s<=%d", "defects2elements", "defect_id", 7, "mat_elem_id", 16));
         var1.execSQL(String.format("UPDATE %s SET manually_added=1, uploaded=0 WHERE %s>%d", "defects", "_id", 7));
         var1.execSQL(String.format("UPDATE %s SET manually_added=0 WHERE %s<=%d AND %s<=%d", "defects2reasons", "defect_id", 3, "reason_id", 3));
         var1.execSQL(String.format("UPDATE %s SET manually_added=1, uploaded=0 WHERE %s>%d", "reasons", "_id", 3));
         var1.execSQL(String.format("UPDATE %s SET manually_added=0 WHERE %s<=%d AND %s<=%d", "reasons2compensations", "reason_id", 3, "compensation_id", 5));
         var1.execSQL(String.format("UPDATE %s SET manually_added=1, uploaded=0 WHERE %s>%d", "compensations", "_id", 5));
      }

   }
}
