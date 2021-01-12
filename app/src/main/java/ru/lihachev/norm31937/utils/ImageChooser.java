package ru.lihachev.norm31937.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageChooser {
   private static final int CHOOSE_PHOTO = 1;
   private static final String[] ITEMS = {"Сфотографировать", "Выбрать из галереи"};
   private static final int REQUEST_CODE_CHOOSE_PHOTO = 9814;
   private static final int REQUEST_CODE_TAKE_PHOTO = 9812;
   private static final int TAKE_PHOTO = 0;
   /* access modifiers changed from: private */
   public Activity act;
   File subtarget;

   public ImageChooser(Activity act2) {
      this.act = act2;
   }

   private static Uri extractGooglePhoto(Uri uri) {
      if (!"com.google.android.apps.photos.contentprovider".equals(uri.getAuthority())) {
         return uri;
      }
      for (String segment : uri.getPathSegments()) {
         if (segment.startsWith("content://media/external/images/media")) {
            return Uri.parse(segment);
         }
      }
      return uri;
   }

   private static Uri writeToTempImageAndGetPathUri(Context context, Uri uri) {
      InputStream is = null;
      try {
         is = context.getContentResolver().openInputStream(uri);
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         Bitmap bmp = BitmapFactory.decodeStream(is);
         bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
         uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "Title", (String) null));
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      } catch (FileNotFoundException e2) {
         e2.printStackTrace();
         if (is != null) {
            try {
               is.close();
            } catch (IOException e3) {
               e3.printStackTrace();
            }
         }
      } catch (Throwable th) {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e4) {
               e4.printStackTrace();
            }
         }
         throw th;
      }
      return uri;
   }

   public void requestImageSelection(final boolean urgent) {
      new AlertDialog.Builder(this.act).setItems(ITEMS, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            switch (which) {
               case 0:
                  File store = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ImageChooser.this.act.getPackageName());
                  if (store.exists() || store.mkdirs()) {
                     ImageChooser.this.subtarget = new File(store, "RAW_IMAGE_" + System.currentTimeMillis() + ".png");
                     Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                     takePictureIntent.putExtra("output", Uri.fromFile(ImageChooser.this.subtarget));
                     ImageChooser.this.act.startActivityForResult(takePictureIntent, ImageChooser.REQUEST_CODE_TAKE_PHOTO);
                     return;
                  }
                  throw new RuntimeException("Cannot create pictures dir");
               case 1:
                  Intent choosePhotoIntent = new Intent("android.intent.action.GET_CONTENT");
                  choosePhotoIntent.setType("image/*");
                  ImageChooser.this.act.startActivityForResult(choosePhotoIntent, ImageChooser.REQUEST_CODE_CHOOSE_PHOTO);
                  return;
               default:
                  return;
            }
         }
      }).setOnCancelListener(new DialogInterface.OnCancelListener() {
         public void onCancel(DialogInterface dialog) {
            if (urgent) {
               ImageChooser.this.act.finish();
            }
         }
      }).show();
   }

   @Nullable
   public Uri handleResponce(int requestCode, int responseCode, Intent data) {
      if (responseCode != -1) {
         return null;
      }
      switch (requestCode) {
         case REQUEST_CODE_TAKE_PHOTO /*9812*/:
            return Uri.fromFile(this.subtarget);
         case REQUEST_CODE_CHOOSE_PHOTO /*9814*/:
            return writeToTempImageAndGetPathUri(this.act, extractGooglePhoto(data.getData()));
         default:
            return null;
      }
   }
}
