package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class ArrowFImagePatch implements ImagePatch {
   private static final float COS120 = (float)Math.cos(Math.toRadians(120.0D));
   private static final Paint PAINT = new Paint(1);
   private static final float SIN120 = (float)Math.sin(Math.toRadians(120.0D));
   private final PointF a;
   private final PointF b;

   static {
      PAINT.setColor(-65536);
      PAINT.setStyle(Style.FILL_AND_STROKE);
   }

   public ArrowFImagePatch(PointF var1, PointF var2) {
      this.a = var1;
      this.b = var2;
   }

   public void draw(Painting var1, Canvas var2) {
      PAINT.setStrokeWidth(var1.getDensity() * 2.0F);
      PAINT.setStrokeJoin(Join.MITER);
      PAINT.setStrokeMiter(1.0F);
      var2.drawLine(this.a.x, this.a.y, this.b.x, this.b.y, PAINT);
      float var3 = (float)Math.sqrt(Math.pow((double)(this.a.x - this.b.x), 2.0D) + Math.pow((double)(this.a.y - this.b.y), 2.0D));
      float var4 = -(this.a.x - this.b.x) / var3;
      float var5 = -(this.a.y - this.b.y) / var3;
      float var6 = COS120;
      float var7 = SIN120;
      float var8 = COS120;
      float var9 = SIN120;
      float var10 = COS120;
      float var11 = SIN120;
      float var12 = COS120;
      float var13 = SIN120;
      float var14 = var1.getDensity() * 3.0F;
      float var15 = this.b.x;
      float var16 = this.b.y;
      float var17 = this.b.x;
      var3 = this.b.y;
      float var18 = this.b.x;
      float var19 = this.b.y;
      float var20 = this.b.x;
      float var21 = this.b.y;
      Paint var22 = PAINT;
      var2.drawLines(new float[]{var4 * var14 + var15, var5 * var14 + var16, (var6 * var4 + var7 * var5) * var14 + var17, (var8 * var5 - var9 * var4) * var14 + var3, (var10 * var4 - var11 * var5) * var14 + var18, (var12 * var5 + var13 * var4) * var14 + var19, var4 * var14 + var20, var5 * var14 + var21}, var22);
   }

   public void set(PointF var1, PointF var2) {
      this.a.set(var1);
      this.b.set(var2);
   }
}
