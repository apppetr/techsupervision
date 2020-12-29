package ru.sviridov.techsupervision.pictures;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.impl.SimpleJSONMaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import nl.qbusict.cupboard.CupboardFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sviridov.techsupervision.Metrics;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.utils.ImageChooser;
import ru.sviridov.techsupervision.utils.SelectListener;
import ru.sviridov.techsupervision.utils.alerts.Dialogs;
import ru.sviridov.techsupervision.utils.alerts.SelectDialogs;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.PaintView;
import ru.sviridov.techsupervision.utils.vectors.Painter;
import ru.sviridov.techsupervision.utils.vectors.impl.NumberCircleImagePatch;
import ru.sviridov.techsupervision.utils.vectors.impl.TextImagePatch;
import ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotArrowInstrument;
import ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotInstrument;
import ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotLineInstrument;
import ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotOvalInstrument;

public class PictureEditActivity extends ToolbarActivity implements RequestPermissionCallback {
   private static final int INDEX_ADD_ARROW = 2;
   private static final int INDEX_ADD_LINE = 0;
   private static final int INDEX_ADD_OVAL = 1;
   public static final String PICTURE_ID = "ru.sviridov.techsupervision.pictures.PICTURE_ID";
   private static final int REQUEST_PERMISSIONS = 1112;
   private static final String[] figures = new String[]{"Добавить линию", "Добавить овал", "Добавить стрелку"};
   Map associatedData = new HashMap();
   ImageChooser chooser = new ImageChooser(this);
   JSONMaker maker = new SimpleJSONMaker();
   private long openedTime;
   PaintView paintView;
   private PermissionController permissionController;
   private Picture picture;

   public PictureEditActivity() throws JSONException {
      this.maker.add("entity", "{'path': *, 'cm': *}");
      this.maker.add("comment", "{'mark': *, 'desc': *, 'time': *}");
   }

   private void addPicture(String var1) {
      this.picture.setImgUrl(var1);
      Uri var2 = UserDataProvider.getContentUri("Picture");
      var2 = CupboardFactory.cupboard().withContext(this).put(var2, this.picture);
      this.picture.setId(Long.parseLong(var2.getLastPathSegment()));
   }

   private void addPivotGeom(PivotInstrument var1, JSONObject var2) {
      this.associatedData.put(var1, var2);
      this.paintView.addPatch(var1);
      PaintView var3 = this.paintView;
      String var5 = var2.optString("mark");
      float var4 = ((PointF)var1.pivots.get(0)).x;
      var3.addPatch(new TextImagePatch(var5, this.paintView.getDensity() * 15.0F + var4, ((PointF)var1.pivots.get(0)).y));
      this.paintView.addPatch(new NumberCircleImagePatch(this.associatedData.size(), ((PointF)var1.pivots.get(0)).x - this.paintView.getDensity() * 15.0F, ((PointF)var1.pivots.get(0)).y));
      this.updateCommentCounter();
   }

   private void delete() {
      if (this.picture.getId() != null) {
         Uri var1 = UserDataProvider.getContentUri("Picture");
         CupboardFactory.cupboard().withContext(this).delete(var1, this.picture);
      }
   }

   private void deleteMarks() throws JSONException {
      this.paintView.getUndoBuffer().clear();
      this.associatedData.clear();
      this.paintView.invalidate();
      this.updatePicture();
      this.updateCommentCounter();
      this.getSupportFragmentManager().popBackStackImmediate();
   }

   private void initInstruments(final Picture var1) {
      this.paintView.post(new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      });
   }

   private void updateCommentCounter() {
      TextView var1 = (TextView)this.findViewById(R.id.tvComments);
      byte var2;
      if (this.associatedData.size() > 0) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      var1.setVisibility(var2);
      var1.setText("Комментарии: " + this.associatedData.size());
   }

   private void updatePicture() throws JSONException {
      this.picture.setGeometries(this.formGeometries());
      Uri var1 = UserDataProvider.getContentUri("Picture");
      ContentValues var2 = CupboardFactory.cupboard().withEntity(Picture.class).toContentValues(this.picture);
      CupboardFactory.cupboard().withContext(this).update(var1, var2);
   }

   public void addComment(View var1) {
      (new AlertDialog.Builder(this)).setItems(figures, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            switch(var2) {
            case 0:
             //  Mint.logEvent("add geometry", MintLogLevel.Info, "type", "line");
               try {
                  PictureEditActivity.this.paintView.setCurrentInstrument(new PivotLineInstrument());
               } catch (JSONException e) {
                  e.printStackTrace();
               }
               break;
            case 1:
            //   Mint.logEvent("add geometry", MintLogLevel.Info, "type", "circle");
               try {
                  PictureEditActivity.this.paintView.setCurrentInstrument(new PivotOvalInstrument());
               } catch (JSONException e) {
                  e.printStackTrace();
               }
               break;
            case 2:
             //  Mint.logEvent("add geometry", MintLogLevel.Info, "type", "arrow");
               try {
                  PictureEditActivity.this.paintView.setCurrentInstrument(new PivotArrowInstrument());
               } catch (JSONException e) {
                  e.printStackTrace();
               }
            }

            PictureEditActivity.this.editMode(true);
         }
      }).show();
   }

   protected void displayImage(@Nullable Uri var1) {
      if (var1 != null) {
         IOException var10000;
         label94: {
            Options var2;
            boolean var10001;
            label89: {
               try {
                  var2 = new Options();
                  var2.inJustDecodeBounds = true;
                  InputStream var3 = this.getContentResolver().openInputStream(var1);
                  BitmapFactory.decodeStream(var3, (Rect)null, var2);
                  var3.close();
                  var2.inSampleSize = 2;
                  if (var2.outHeight / this.paintView.getHeight() <= 1 && var2.outWidth / this.paintView.getWidth() <= 1) {
                     break label89;
                  }
               } catch (IOException var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label94;
               }

               var2.inSampleSize = Math.max(var2.outHeight / this.paintView.getHeight(), var2.outWidth / this.paintView.getWidth());
            }

            Bitmap var19;
            try {
               var2.inJustDecodeBounds = false;
               PrintStream var4 = System.out;
               StringBuilder var18 = new StringBuilder();
               var4.println(var18.append("Sampling ").append(var2.inSampleSize).append(" times").toString());
               InputStream var20 = this.getContentResolver().openInputStream(var1);
               var19 = BitmapFactory.decodeStream(var20, (Rect)null, var2);
               var20.close();
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break label94;
            }

            Bitmap var16 = var19;

            label75: {
               Matrix var14;
               label74: {
                  label73: {
                     label72: {
                        label71: {
                           try {
                              if (!"file".equals(var1.getScheme())) {
                                 break label75;
                              }

                              ExifInterface var17 = new ExifInterface(var1.getPath());
                              var14 = new Matrix();
                              switch(var17.getAttributeInt("Orientation", 0)) {
                              case 3:
                                 break label72;
                              case 4:
                              case 5:
                              case 7:
                              default:
                                 break;
                              case 6:
                                 break label71;
                              case 8:
                                 break label73;
                              }
                           } catch (IOException var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label94;
                           }

                           var14 = null;
                           break label74;
                        }

                        var14.postRotate(90.0F);
                        break label74;
                     }

                     var14.postRotate(180.0F);
                     break label74;
                  }

                  var14.postRotate(270.0F);
               }

               var16 = var19;
               if (var14 != null) {
                  var16 = Bitmap.createBitmap(var19, 0, 0, var19.getWidth(), var19.getHeight(), var14, true);
                  var19.recycle();
               }
            }

            this.paintView.drawOnBase(var16);
            var16.recycle();
            return;
         }

         IOException var15 = var10000;
        // Mint.logException(var15);
      }

   }

   protected void editMode(boolean var1) {
      byte var2 = 0;
      View var3 = this.findViewById(R.id.dataActions);
      byte var4;
      if (var1) {
         var4 = 8;
      } else {
         var4 = 0;
      }

      var3.setVisibility(var4);
      var3 = this.findViewById(R.id.editActions);
      if (var1) {
         var4 = var2;
      } else {
         var4 = 8;
      }

      var3.setVisibility(var4);
   }

   protected JSONArray formGeometries() throws JSONException {
      JSONArray var1 = new JSONArray();
      Iterator var2 = this.paintView.getUndoBuffer().iterator();

      while(var2.hasNext()) {
         ImagePatch var3 = (ImagePatch)var2.next();
         if (var3 instanceof PivotInstrument) {
            JSONObject var4 = new JSONObject();

            try {
               ((PivotInstrument)var3).writeToJSONObject(var4);
            } catch (JSONException var6) {
               //Mint.logException(var6);
            }

            var1.put(this.maker.make("entity", var4, this.associatedData.get(var3)));
         }
      }

      return var1;
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      Uri var4 = this.chooser.handleResponce(var1, var2, var3);
      if (var4 == null) {
         this.finish();
      } else {
         this.addPicture(var4.toString());
         this.displayImage(var4);
      }

   }

   public void onApply(View var1) {
      final PivotInstrument var2 = (PivotInstrument)this.paintView.getCurrentInstrument();
      this.editMode(false);
      this.paintView.setCurrentInstrument((Painter)null);
      SelectDialogs.showMeasumentDialog(this, new SelectListener() {
         @Override
         public void onSelected(Object var1) {

         }

         public void onSelected(final String var1) {
            if (var1 != null) {
               View var2x = LayoutInflater.from(PictureEditActivity.this).inflate(R.layout.photo_comment, (ViewGroup)null);
               OnClickListener var3 = new OnClickListener() {
                  // $FF: synthetic field
                  public void onClick(@NonNull DialogInterface var1x, int var2x) {
                     if (var2x == -1) {
                        JSONObject var3 = null;
                        try {
                           var3 = (JSONObject) PictureEditActivity.this.maker.make("comment", var1, "sometext", System.currentTimeMillis());
                        } catch (JSONException e) {
                           e.printStackTrace();
                        }
                        PictureEditActivity.this.addPivotGeom(var2, var3);
                        PictureEditActivity.this.paintView.postInvalidate();
                        try {
                           PictureEditActivity.this.updatePicture();
                        } catch (JSONException e) {
                           e.printStackTrace();
                        }
                     }

                  }
               };
               Dialogs.showCustomView(PictureEditActivity.this, R.string.mark_comment, var2x, R.string.apply, R.string.cancel, var3);
            }

         }
      }).show();
   }

   public void onBackPressed() {
      if (!this.getSupportFragmentManager().popBackStackImmediate()) {
         super.onBackPressed();
      }

   }

   public void onCancel(View var1) {
      this.paintView.setCurrentInstrument((Painter)null);
      this.editMode(false);
      this.paintView.postInvalidate();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_picture_edit);
      this.findViewById(R.id.tvAddComment).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            PictureEditActivity.this.addComment(var1);
         }
      });
      this.findViewById(R.id.tvComments).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(@NonNull View var1) {
            PictureEditActivity.this.showComments(var1);
         }
      });
      this.paintView = (PaintView)this.findViewById(R.id.pvPaint);
      this.paintView.setUndoLimit(Integer.MAX_VALUE);
      this.permissionController = new PermissionController(this);
      if (this.permissionController.isPermissionGranted(PermissionType.WRITE_EXTERNAL_STORAGE) && this.permissionController.isPermissionGranted(PermissionType.GOOGLE_PHOTOS)) {
         this.onPermissionGranted(1112);
      } else {
         this.permissionController.requestGroupOfUserGrantPermission(1112, PermissionType.WRITE_EXTERNAL_STORAGE, PermissionType.GOOGLE_PHOTOS);
      }

      this.openedTime = System.currentTimeMillis();
      //Mint.logEvent("open picture edit", MintLogLevel.Info);
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(R.menu.picture_edit_menu, var1);
      return super.onCreateOptionsMenu(var1);
   }

   protected void onDestroy() {

      super.onDestroy();
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      switch(var1.getItemId()) {
      case R.id.info:
         break;
      case R.id.delete:
         this.delete();
         this.finish();
         break;
      case R.id.delete_marks:
         try {
            this.deleteMarks();
         } catch (JSONException e) {
            e.printStackTrace();
         }
         break;
      default:
         this.onBackPressed();
      }

      return super.onOptionsItemSelected(var1);
   }

   public void onPermissionDenied(int var1) {
      Toast.makeText(this, "Работа с изображениями невозможна без доступа к ресурсам", Toast.LENGTH_LONG).show();
      this.finish();
   }

   public void onPermissionGranted(int var1) {
      if (this.getIntent().hasExtra("ru.sviridov.techsupervision.pictures.PICTURE_ID")) {
         var1 = this.getIntent().getIntExtra("ru.sviridov.techsupervision.pictures.PICTURE_ID", -1);
         Uri var2 = ContentUris.withAppendedId(UserDataProvider.getContentUri("Picture"), (long)var1);
         this.picture = (Picture)CupboardFactory.cupboard().withContext(this).query(var2, Picture.class).get();
         this.initInstruments(this.picture);
      } else {
         var1 = this.getIntent().getIntExtra("ru.sviridov.techsupervision.DEFECT_ID", -1);
         this.picture = new Picture();
         this.picture.setDefectId(var1);
         this.chooser.requestImageSelection(true);
      }

   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);
      this.permissionController.onRequestPermissionsResult(var1, var2, var3, this);
   }

   public void showComments(View var1) {
      final EditCommentsFragment var2 = new EditCommentsFragment();
      this.getSupportFragmentManager().beginTransaction().addToBackStack("elements").add(R.id.root, var2).commit();
      var1.post(new Runnable() {
         public void run() {
            var2.setComments(PictureEditActivity.this.picture.getComments());
         }
      });
    //  Mint.logEvent("open comments", MintLogLevel.Info);
   }
}
