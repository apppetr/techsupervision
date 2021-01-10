package ru.sviridov.techsupervision.pictures;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActivityChooserView;
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
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import fr.opensagres.xdocreport.document.docx.DocxConstants;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.Metrics;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.defects.AddDefectActivity;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.db.UserDataHelper;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.utils.ImageChooser;
import ru.sviridov.techsupervision.utils.JSONUtils;
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
import android.support.v7.widget.ActivityChooserView;
/* renamed from: ru.sviridov.techsupervision.pictures.PictureEditActivity */
public class PictureEditActivity extends ToolbarActivity implements RequestPermissionCallback {
   private static final int INDEX_ADD_ARROW = 2;
   private static final int INDEX_ADD_LINE = 0;
   private static final int INDEX_ADD_OVAL = 1;
   public static final String PICTURE_ID = "ru.sviridov.techsupervision.pictures.PICTURE_ID";
   private static final int REQUEST_PERMISSIONS = 1112;
   private static final String[] figures = {"Добавить линию", "Добавить овал", "Добавить стрелку"};
   Map<PivotInstrument, JSONObject> associatedData = new HashMap();
   ImageChooser chooser = new ImageChooser(this);
   JSONMaker maker = new SimpleJSONMaker();
   private long openedTime;
   PaintView paintView;
   private PermissionController permissionController;
   /* access modifiers changed from: private */
   public Picture picture;

   public PictureEditActivity() throws JSONException {
      this.maker.add("entity", "{'path': *, 'cm': *}");
      this.maker.add("comment", "{'mark': *, 'desc': *, 'time': *}");
   }

   /* access modifiers changed from: protected */
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView((int) R.layout.activity_picture_edit);
      findViewById(R.id.tvAddComment).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            PictureEditActivity.this.addComment(v);
         }
      });
      findViewById(R.id.tvComments).setOnClickListener(new View.OnClickListener() {
         public void onClick(@NonNull View view) {
            PictureEditActivity.this.showComments(view);
         }
      });
      this.paintView = (PaintView) findViewById(R.id.pvPaint);
      this.paintView.setUndoLimit(20);
      this.permissionController = new PermissionController(this);
      if (!this.permissionController.isPermissionGranted(PermissionType.WRITE_EXTERNAL_STORAGE) || !this.permissionController.isPermissionGranted(PermissionType.GOOGLE_PHOTOS)) {
         this.permissionController.requestGroupOfUserGrantPermission(REQUEST_PERMISSIONS, PermissionType.WRITE_EXTERNAL_STORAGE, PermissionType.GOOGLE_PHOTOS);
      } else {
         onPermissionGranted(REQUEST_PERMISSIONS);
      }
      this.openedTime = System.currentTimeMillis();
   //  Mint.logEvent(Metrics.OPEN_PICTURE_EDIT, MintLogLevel.Info);
   }

   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      this.permissionController.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
   }

   public void onPermissionGranted(int requestCode) {
      if (getIntent().hasExtra(PICTURE_ID)) {
         this.picture = CupboardFactory.cupboard().withContext(this).query(ContentUris.withAppendedId(UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), (long) getIntent().getIntExtra(PICTURE_ID, -1)), Picture.class).get();
         initInstruments(this.picture);
         return;
      }
      int defectId = getIntent().getIntExtra(AddDefectActivity.DEFECT_ID, -1);
      this.picture = new Picture();
      this.picture.setDefectId(defectId);
      this.chooser.requestImageSelection(true);
   }

   public void onPermissionDenied(int requestCode) {
      Toast.makeText(this, "Работа с изображениями невозможна без доступа к ресурсам", Toast.LENGTH_LONG).show();
      finish();
   }

   private void initInstruments(final Picture picture) {
      this.paintView.post(new Runnable() {
         public void run() {
            PictureEditActivity.this.displayImage(Uri.parse(picture.getImgUrl()));
            if (picture.getGeometries() != null) {
               for (Object object : JSONUtils.iterate(picture.getGeometries())) {

                  try {
                     JSONObject jobj = (JSONObject)  object;
                     PivotInstrument geom = (PivotInstrument) PivotArrowInstrument.JSON_CREATOR.createFromJSONObject(jobj.optJSONObject("path"));

                     PictureEditActivity.this.addPivotGeom(geom, jobj.getJSONObject("cm"));

                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
             }
            }
         }
      });
   }

   private void updateCommentCounter() {
      TextView comment_button = (TextView) findViewById(R.id.tvComments);
      comment_button.setVisibility(this.associatedData.size() > 0 ? View.VISIBLE : View.GONE);
      comment_button.setText("Комментарии: " + this.associatedData.size());
   }

   /* access modifiers changed from: private */
   public void addPivotGeom(PivotInstrument geom, JSONObject assoc) {
      this.associatedData.put(geom, assoc);
      this.paintView.addPatch(geom);
      this.paintView.addPatch(new TextImagePatch(assoc.optString("mark"), (this.paintView.getDensity() * 15.0f) + geom.pivots.get(0).x, geom.pivots.get(0).y));
      this.paintView.addPatch(new NumberCircleImagePatch(this.associatedData.size(), geom.pivots.get(0).x - (this.paintView.getDensity() * 15.0f), geom.pivots.get(0).y));
      updateCommentCounter();
   }

   public void addComment(View view) {
      new AlertDialog.Builder(this).setItems((CharSequence[]) figures, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            switch (which) {
               case 0:
                //  Mint.logEvent(Metrics.ADD_GEOMETRY, MintLogLevel.Info, DocxConstants.TYPE_ATTR, "line");
                  try {
                     PictureEditActivity.this.paintView.setCurrentInstrument(new PivotLineInstrument());
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  break;
               case 1:
                //  Mint.logEvent(Metrics.ADD_GEOMETRY, MintLogLevel.Info, DocxConstants.TYPE_ATTR, "circle");
                  try {
                     PictureEditActivity.this.paintView.setCurrentInstrument(new PivotOvalInstrument());
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  break;
               case 2:
               //   Mint.logEvent(Metrics.ADD_GEOMETRY, MintLogLevel.Info, DocxConstants.TYPE_ATTR, "arrow");
                  try {
                     PictureEditActivity.this.paintView.setCurrentInstrument(new PivotArrowInstrument());
                  } catch (JSONException e) {
                     e.printStackTrace();
                  }
                  break;
            }
            PictureEditActivity.this.editMode(true);
         }
      }).show();
   }

   /* access modifiers changed from: protected */
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Uri imageUri = this.chooser.handleResponce(requestCode, resultCode, data);
      if (imageUri == null) {
         finish();
         return;
      }
      addPicture(imageUri.toString());
      displayImage(imageUri);
   }

   /* access modifiers changed from: protected */
   public void displayImage(@Nullable Uri imageUri) {
      if (imageUri != null) {
         try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            InputStream is = getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(is, (Rect) null, opt);
            is.close();
            opt.inSampleSize = 2;
            if (opt.outHeight / this.paintView.getHeight() > 1 || opt.outWidth / this.paintView.getWidth() > 1) {
               opt.inSampleSize = Math.max(opt.outHeight / this.paintView.getHeight(), opt.outWidth / this.paintView.getWidth());
            }
            opt.inJustDecodeBounds = false;
            System.out.println("Sampling " + opt.inSampleSize + " times");
            InputStream is2 = getContentResolver().openInputStream(imageUri);
            Bitmap bmp = BitmapFactory.decodeStream(is2, (Rect) null, opt);
            is2.close();
            if ("file".equals(imageUri.getScheme())) {
               ExifInterface exifInterface = new ExifInterface(imageUri.getPath());
               Matrix rotate = new Matrix();
               switch (exifInterface.getAttributeInt("Orientation", 0)) {
                  case 3:
                     rotate.postRotate(180.0f);
                     break;
                  case 6:
                     rotate.postRotate(90.0f);
                     break;
                  case 8:
                     rotate.postRotate(270.0f);
                     break;
                  default:
                     rotate = null;
                     break;
               }
               if (rotate != null) {
                  Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), rotate, true);
                  bmp.recycle();
                  bmp = rotated;
               }
            }
            this.paintView.drawOnBase(bmp);
            bmp.recycle();
         } catch (IOException e) {
        //    Mint.logException(e);
         }
      }
   }

   public void onCancel(View v) {
      this.paintView.setCurrentInstrument((Painter) null);
      editMode(false);
      this.paintView.postInvalidate();
   }

   public void onApply(View v) {
      final PivotInstrument patch = (PivotInstrument) this.paintView.getCurrentInstrument();
      editMode(false);
      this.paintView.setCurrentInstrument((Painter) null);
      SelectDialogs.showMeasumentDialog(this, new SelectListener() {
         @Override
         public void onSelected(final Object selection) {
            if (selection != null) {
               View view = LayoutInflater.from(PictureEditActivity.this).inflate(R.layout.photo_comment, (ViewGroup) null);
               final EditText etText = (EditText) view.findViewById(R.id.etText);
               Dialogs.showCustomView(PictureEditActivity.this, R.string.mark_comment, view, R.string.apply, R.string.cancel, new DialogInterface.OnClickListener() {
                  public void onClick(@NonNull DialogInterface dialog, int which) {
                     if (which == -1) {
                        JSONMaker jSONMaker = PictureEditActivity.this.maker;
                        Object[] objArr = {selection, etText.getText().toString(), Long.valueOf(System.currentTimeMillis())};
                        try {
                           PictureEditActivity.this.addPivotGeom(patch, (JSONObject) jSONMaker.make("comment", objArr));
                        } catch (JSONException e) {
                           e.printStackTrace();
                        }
                        PictureEditActivity.this.paintView.postInvalidate();
                        try {
                           PictureEditActivity.this.updatePicture();
                        } catch (JSONException e) {
                           e.printStackTrace();
                        }
                     }
                  }
               });
            }
         }
      }).show();
   }

   public void showComments(View view) {
      final EditCommentsFragment commentsFragment = new EditCommentsFragment();
      getSupportFragmentManager().beginTransaction().addToBackStack("elements").add((int) R.id.root, (Fragment) commentsFragment).commit();
      view.post(new Runnable() {
         public void run() {
            commentsFragment.setComments(PictureEditActivity.this.picture.getComments());
         }
      });
     // Mint.logEvent(Metrics.OPEN_COMMENTS, MintLogLevel.Info);
   }

   public void onBackPressed() {
      if (!getSupportFragmentManager().popBackStackImmediate()) {
         super.onBackPressed();
      }
   }

   /* access modifiers changed from: protected */
   public JSONArray formGeometries() throws JSONException {
      JSONArray geometries = new JSONArray();
      for (ImagePatch patch : this.paintView.getUndoBuffer()) {
         if (patch instanceof PivotInstrument) {
            JSONObject patchData = new JSONObject();
            try {
               ((PivotInstrument) patch).writeToJSONObject(patchData);
            } catch (JSONException e) {
            //   Mint.logException(e);
            }
            geometries.put(this.maker.make("entity", patchData, this.associatedData.get(patch)));
         }
      }
      return geometries;
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.picture_edit_menu, menu);
      return super.onCreateOptionsMenu(menu);
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.info /*2131558592*/:
            break;
         case R.id.delete /*2131558604*/:
            delete();
            finish();
            break;
         case R.id.delete_marks /*2131558605*/:
            try {
               deleteMarks();
            } catch (JSONException e) {
               e.printStackTrace();
            }
            break;
         default:
            onBackPressed();
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   /* access modifiers changed from: protected */
   public void editMode(boolean on) {
      int i;
      int i2 = 0;
      View findViewById = findViewById(R.id.dataActions);
      if (on) {
         i = 8;
      } else {
         i = 0;
      }
      findViewById.setVisibility(i);
      View findViewById2 = findViewById(R.id.editActions);
      if (!on) {
         i2 = 8;
      }
      findViewById2.setVisibility(i2);
   }

   private void addPicture(String imageUri) {
      this.picture.setImgUrl(imageUri);
      this.picture.setId(Long.valueOf(Long.parseLong(CupboardFactory.cupboard().withContext(this).put(UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), this.picture).getLastPathSegment())));
   }

   /* access modifiers changed from: private */
   public void updatePicture() throws JSONException {
      this.picture.setGeometries(formGeometries());
      CupboardFactory.cupboard().withContext(this).update(UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), CupboardFactory.cupboard().withEntity(Picture.class).toContentValues(this.picture));
   }

   private void delete() {
      if (this.picture.getId() != null) {
         CupboardFactory.cupboard().withContext(this).delete(UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), this.picture);
      }
   }

   private void deleteMarks() throws JSONException {
      this.paintView.getUndoBuffer().clear();
      this.associatedData.clear();
      this.paintView.invalidate();
      updatePicture();
      updateCommentCounter();
      getSupportFragmentManager().popBackStackImmediate();
   }

   /* access modifiers changed from: protected */
   public void onDestroy() {
      if (this.picture != null) {
      //   Mint.logEvent(Metrics.FINISH_PICTURE_EDIT, MintLogLevel.Info, Metrics.toMetrics(this.picture, this.openedTime));
      } else {
       //  Mint.logEvent(Metrics.FINISH_PICTURE_PERMISSIONS_DENIED, MintLogLevel.Info);
      }
      super.onDestroy();
   }
}