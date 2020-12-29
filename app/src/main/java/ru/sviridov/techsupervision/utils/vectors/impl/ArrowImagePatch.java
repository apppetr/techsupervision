package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class ArrowImagePatch implements ImagePatch {
   private static final float COS120 = (float)Math.cos(Math.toRadians(120.0D));
   private static final Paint PAINT = new Paint(1);
   private static final float SIN120 = (float)Math.sin(Math.toRadians(120.0D));
   private final Point a;
   private final Point b;

   static {
      PAINT.setColor(-1430519809);
      PAINT.setStyle(Style.FILL_AND_STROKE);
   }

   public ArrowImagePatch(Point var1, Point var2) {
      this.a = var1;
      this.b = var2;
   }

   public void draw(Painting var1, Canvas var2) {
      PAINT.setStrokeWidth(var1.getDensity() * 3.0F);
      var2.drawLine((float)this.a.x, (float)this.a.y, (float)this.b.x, (float)this.b.y, PAINT);
      float var3 = (float)Math.sqrt(Math.pow((double)(this.a.x - this.b.x), 2.0D) + Math.pow((double)(this.a.y - this.b.y), 2.0D));
      float var4 = (float)(-(this.a.x - this.b.x)) / var3;
      float var5 = (float)(-(this.a.y - this.b.y)) / var3;
      float var6 = COS120;
      float var7 = SIN120;
      float var8 = COS120;
      float var9 = SIN120;
      float var10 = COS120;
      float var11 = SIN120;
      float var12 = COS120;
      float var13 = SIN120;
      float var14 = var1.getDensity() * 10.0F;
      float var15 = (float)this.b.x;
      var3 = (float)this.b.y;
      float var16 = (float)this.b.x;
      float var17 = (float)this.b.y;
      float var18 = (float)this.b.x;
      float var19 = (float)this.b.y;
      float var20 = (float)this.b.x;
      float var21 = (float)this.b.y;
      Paint var22 = PAINT;
      var2.drawLines(new float[]{var4 * var14 + var15, var5 * var14 + var3, (var6 * var4 + var7 * var5) * var14 + var16, (var8 * var5 - var9 * var4) * var14 + var17, (var10 * var4 - var11 * var5) * var14 + var18, (var12 * var5 + var13 * var4) * var14 + var19, var4 * var14 + var20, var5 * var14 + var21}, var22);
   }
}
