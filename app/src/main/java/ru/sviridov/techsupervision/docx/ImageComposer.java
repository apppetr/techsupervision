package ru.sviridov.techsupervision.docx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.DisplayMetrics;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;
import ru.sviridov.techsupervision.utils.vectors.impl.NumberCircleImagePatch;
import ru.sviridov.techsupervision.utils.vectors.impl.TextImagePatch;
import ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotArrowInstrument;
import ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotInstrument;

public class ImageComposer {
   private final Context ctx;
   private final File outputDir;

   public ImageComposer(File var1, Context var2) {
      this.outputDir = var1;
      this.ctx = var2;
   }

   public void compose(Picture var1) throws IOException {
      if (var1.getGeometries() != null && var1.getGeometries().length() != 0) {
         DisplayMetrics var2 = this.ctx.getResources().getDisplayMetrics();
         Bitmap var3 = Bitmap.createBitmap(var2.widthPixels, var2.heightPixels, Config.ARGB_8888);
         Canvas var4 = new Canvas(var3);
         var3.eraseColor(0);
         Uri var5 = Uri.parse(var1.getImgUrl());
         File var6 = File.createTempFile("image" + var1.getId(), ".png", this.outputDir);
         BufferedInputStream var14 = new BufferedInputStream(this.ctx.getContentResolver().openInputStream(var5), 1024);
         Bitmap var7 = BitmapFactory.decodeStream(var14, (Rect)null, new Options());
         var14.close();
         Bitmap var15 = var7;
         if ("file".equals(var5.getScheme())) {
            ExifInterface var16 = new ExifInterface(var5.getPath());
            Matrix var19 = new Matrix();
            switch(var16.getAttributeInt("Orientation", 0)) {
            case 3:
               var19.postRotate(180.0F);
               break;
            case 4:
            case 5:
            case 7:
            default:
               var19 = null;
               break;
            case 6:
               var19.postRotate(90.0F);
               break;
            case 8:
               var19.postRotate(270.0F);
            }

            var15 = var7;
            if (var19 != null) {
               var15 = Bitmap.createBitmap(var7, 0, 0, var7.getWidth(), var7.getHeight(), var19, true);
               var7.recycle();
            }
         }

         int var8 = (int)((float)var3.getWidth() / ((float)var15.getWidth() / (float)var15.getHeight()));
         var8 = (var3.getHeight() - var8) / 2;
         Rect var20 = new Rect();
         var20.set(0, var8, var3.getWidth(), var3.getHeight() - var8);
         var4.drawBitmap(var15, (Rect)null, var20, new Paint(3));
         var15.recycle();
         ImageComposer.PaintingReplacement var17 = new ImageComposer.PaintingReplacement(this.ctx, var4);
         ArrayList var23 = new ArrayList();
         byte var9 = 0;
         JSONArray var10 = var1.getGeometries();
         var8 = 0;

         for(int var11 = var10.length(); var8 < var11; ++var8) {
            JSONObject var12 = var10.optJSONObject(var8);
            PivotInstrument var21 = (PivotInstrument)PivotArrowInstrument.JSON_CREATOR.createFromJSONObject(var12.optJSONObject("path"));
            var23.add(new SimpleEntry(var12, var21));
            var21.onDraw(var17, var4);
         }

         Iterator var22 = var23.iterator();
         var8 = var9;

         while(var22.hasNext()) {
            Entry var25 = (Entry)var22.next();
            PivotInstrument var24 = (PivotInstrument)var25.getValue();
            String var26 = ((JSONObject)var25.getKey()).optString("mark");
            float var13 = ((PointF)var24.pivots.get(0)).x;
            (new TextImagePatch(var26, 15.0F * var17.getDensity() + var13, ((PointF)var24.pivots.get(0)).y)).draw(var17, var4);
            ++var8;
            (new NumberCircleImagePatch(var8, ((PointF)var24.pivots.get(0)).x - 15.0F * var17.getDensity(), ((PointF)var24.pivots.get(0)).y)).draw(var17, var4);
         }

         BufferedOutputStream var18 = new BufferedOutputStream(new FileOutputStream(var6));
         var3.compress(CompressFormat.PNG, 0, var18);
         var18.close();
         var1.setImgUrl(var6.toString());
      }

   }

   public File getOutputDir() {
      return this.outputDir;
   }

   protected class PaintingReplacement implements Painting {
      private final Context context;
      private final Canvas dstDraw;

      public PaintingReplacement(Context var2, Canvas var3) {
         this.context = var2;
         this.dstDraw = var3;
      }

      public void addPatch(ImagePatch var1) {
      }

      public Canvas getBuffer() {
         return this.dstDraw;
      }

      public Context getContext() {
         return ImageComposer.this.ctx;
      }

      public float getDensity() {
         return this.context.getResources().getDisplayMetrics().density;
      }

      public void postInvalidate() {
      }
   }
}
