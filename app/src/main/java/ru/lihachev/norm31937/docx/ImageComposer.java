package ru.lihachev.norm31937.docx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.DisplayMetrics;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.utils.vectors.ImagePatch;
import ru.lihachev.norm31937.utils.vectors.Painting;
import ru.lihachev.norm31937.utils.vectors.impl.NumberCircleImagePatch;
import ru.lihachev.norm31937.utils.vectors.impl.TextImagePatch;
import ru.lihachev.norm31937.utils.vectors.impl.pivots.PivotArrowInstrument;
import ru.lihachev.norm31937.utils.vectors.impl.pivots.PivotInstrument;

/* renamed from: ru.lihachev.norm31937.docx.ImageComposer */
public class ImageComposer {
   /* access modifiers changed from: private */
   public final Context ctx;
   private final File outputDir;

   public ImageComposer(File outputDir2, Context ctx2) {
      this.outputDir = outputDir2;
      this.ctx = ctx2;
   }

   public File getOutputDir() {
      return this.outputDir;
   }

   public void compose(Picture picture) throws IOException {
      if (picture.getGeometries() != null && picture.getGeometries().length() != 0) {
         DisplayMetrics display = this.ctx.getResources().getDisplayMetrics();
         Bitmap dst = Bitmap.createBitmap(display.widthPixels, display.heightPixels, Bitmap.Config.ARGB_8888);
         Canvas dstDraw = new Canvas(dst);
         dst.eraseColor(0);
         Uri imageUri = Uri.parse(picture.getImgUrl());
         File outPath = File.createTempFile("image" + picture.getId(), ".png", this.outputDir);
         BufferedInputStream bufferedInputStream = new BufferedInputStream(this.ctx.getContentResolver().openInputStream(imageUri), 1024);
         Bitmap image = BitmapFactory.decodeStream(bufferedInputStream, (Rect) null, new BitmapFactory.Options());
         bufferedInputStream.close();
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
               Bitmap rotated = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), rotate, true);
               image.recycle();
               image = rotated;
            }
         }
         int height_padding = (dst.getHeight() - ((int) (((float) dst.getWidth()) / (((float) image.getWidth()) / ((float) image.getHeight()))))) / 2;
         Rect rax = new Rect();
         rax.set(0, height_padding, dst.getWidth(), dst.getHeight() - height_padding);
         dstDraw.drawBitmap(image, (Rect) null, rax, new Paint(3));
         image.recycle();
         PaintingReplacement paintingReplacement = new PaintingReplacement(this.ctx, dstDraw);
         ArrayList<Map.Entry<JSONObject, PivotInstrument>> instruments = new ArrayList<>();
         int index = 0;
         JSONArray geometries = picture.getGeometries();
         int count = geometries.length();
         for (int i = 0; i < count; i++) {
            JSONObject geometry = geometries.optJSONObject(i);
            PivotInstrument geom = (PivotInstrument) PivotArrowInstrument.JSON_CREATOR.createFromJSONObject(geometry.optJSONObject("path"));
            instruments.add(new AbstractMap.SimpleEntry(geometry, geom));
            geom.onDraw(paintingReplacement, dstDraw);
         }
         for (Map.Entry<JSONObject, PivotInstrument> geom2 : instruments) {
            PivotInstrument instrument = geom2.getValue();
            new TextImagePatch(geom2.getKey().optString("mark"), (15.0f * paintingReplacement.getDensity()) + instrument.pivots.get(0).x, instrument.pivots.get(0).y).draw(paintingReplacement, dstDraw);
            index++;
            new NumberCircleImagePatch(index, instrument.pivots.get(0).x - (15.0f * paintingReplacement.getDensity()), instrument.pivots.get(0).y).draw(paintingReplacement, dstDraw);
         }
         BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outPath));
         dst.compress(Bitmap.CompressFormat.PNG, 0, bufferedOutputStream);
         bufferedOutputStream.close();
         picture.setImgUrl(outPath.toString());
      }
   }

   /* renamed from: ru.lihachev.norm31937.docx.ImageComposer$PaintingReplacement */
   protected class PaintingReplacement implements Painting {
      private final Context context;
      private final Canvas dstDraw;

      public PaintingReplacement(Context context2, Canvas dstDraw2) {
         this.context = context2;
         this.dstDraw = dstDraw2;
      }

      public Context getContext() {
         return ImageComposer.this.ctx;
      }

      public Canvas getBuffer() {
         return this.dstDraw;
      }

      public void addPatch(ImagePatch patch) {
      }

      public void postInvalidate() {
      }

      public float getDensity() {
         return this.context.getResources().getDisplayMetrics().density;
      }
   }
}