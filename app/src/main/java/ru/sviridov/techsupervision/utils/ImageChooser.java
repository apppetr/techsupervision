package ru.sviridov.techsupervision.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import java.io.File;
import java.util.Iterator;

public class ImageChooser {
   private static final int CHOOSE_PHOTO = 1;
   private static final String[] ITEMS = new String[]{"Сфотографировать", "Выбрать из галереи"};
   private static final int REQUEST_CODE_CHOOSE_PHOTO = 9814;
   private static final int REQUEST_CODE_TAKE_PHOTO = 9812;
   private static final int TAKE_PHOTO = 0;
   private Activity act;
   File subtarget;

   public ImageChooser(Activity var1) {
      this.act = var1;
   }

   private static Uri extractGooglePhoto(Uri var0) {
      Uri var1 = var0;
      if ("com.google.android.apps.photos.contentprovider".equals(var0.getAuthority())) {
         Iterator var2 = var0.getPathSegments().iterator();

         while(true) {
            var1 = var0;
            if (!var2.hasNext()) {
               break;
            }

            String var3 = (String)var2.next();
            if (var3.startsWith("content://media/external/images/media")) {
               var1 = Uri.parse(var3);
               break;
            }
         }
      }

      return var1;
   }

   private static Uri writeToTempImageAndGetPathUri(Context param0, Uri param1) {
     return null;
   }

   @Nullable
   public Uri handleResponce(int var1, int var2, Intent var3) {
      Object var4 = null;
      Uri var5;
      if (var2 != -1) {
         var5 = (Uri)var4;
      } else {
         switch(var1) {
         case 9812:
            var5 = Uri.fromFile(this.subtarget);
            break;
         case 9813:
         default:
            var5 = (Uri)var4;
            break;
         case 9814:
            var5 = writeToTempImageAndGetPathUri(this.act, extractGooglePhoto(var3.getData()));
         }
      }

      return var5;
   }

   public void requestImageSelection(final boolean var1) {
      (new Builder(this.act)).setItems(ITEMS, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            Intent var3;
            switch(var2) {
            case 0:
               File var4 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ImageChooser.this.act.getPackageName());
               if (!var4.exists() && !var4.mkdirs()) {
                  throw new RuntimeException("Cannot create pictures dir");
               }

               ImageChooser.this.subtarget = new File(var4, "RAW_IMAGE_" + System.currentTimeMillis() + ".png");
               var3 = new Intent("android.media.action.IMAGE_CAPTURE");
               var3.putExtra("output", Uri.fromFile(ImageChooser.this.subtarget));
               ImageChooser.this.act.startActivityForResult(var3, 9812);
               break;
            case 1:
               var3 = new Intent("android.intent.action.GET_CONTENT");
               var3.setType("image/*");
               ImageChooser.this.act.startActivityForResult(var3, 9814);
            }

         }
      }).setOnCancelListener(new OnCancelListener() {
         public void onCancel(DialogInterface var1x) {
            if (var1) {
               ImageChooser.this.act.finish();
            }

         }
      }).show();
   }
}
