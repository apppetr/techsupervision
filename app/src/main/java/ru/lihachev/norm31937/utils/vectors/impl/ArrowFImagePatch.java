package ru.lihachev.norm31937.utils.vectors.impl;
import android.support.v4.internal.view.SupportMenu;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import ru.lihachev.norm31937.utils.vectors.ImagePatch;
import ru.lihachev.norm31937.utils.vectors.Painting;

public class ArrowFImagePatch implements ImagePatch {
   private static final float COS120 = ((float) Math.cos(Math.toRadians(120.0d)));
   private static final Paint PAINT = new Paint(1);
   private static final float SIN120 = ((float) Math.sin(Math.toRadians(120.0d)));

   /* renamed from: a */
   private final PointF f78a;

   /* renamed from: b */
   private final PointF f79b;

   static {
      PAINT.setColor(SupportMenu.CATEGORY_MASK);
      PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
   }

   public ArrowFImagePatch(PointF a, PointF b) {
      this.f78a = a;
      this.f79b = b;
   }

   public void set(PointF a, PointF b) {
      this.f78a.set(a);
      this.f79b.set(b);
   }

   public void draw(Painting target, Canvas cvs) {
      PAINT.setStrokeWidth(target.getDensity() * 2.0f);
      PAINT.setStrokeJoin(Paint.Join.MITER);
      PAINT.setStrokeMiter(1.0f);
      cvs.drawLine(this.f78a.x, this.f78a.y, this.f79b.x, this.f79b.y, PAINT);
      float hypo = (float) Math.sqrt(Math.pow((double) (this.f78a.x - this.f79b.x), 2.0d) + Math.pow((double) (this.f78a.y - this.f79b.y), 2.0d));
      float sin1 = (-(this.f78a.x - this.f79b.x)) / hypo;
      float cos1 = (-(this.f78a.y - this.f79b.y)) / hypo;
      float sin2 = (COS120 * sin1) + (SIN120 * cos1);
      float cos2 = (COS120 * cos1) - (SIN120 * sin1);
      float sin3 = (COS120 * sin1) - (SIN120 * cos1);
      float cos3 = (COS120 * cos1) + (SIN120 * sin1);
      float radius = target.getDensity() * 3.0f;
      Canvas canvas = cvs;
      canvas.drawLines(new float[]{(sin1 * radius) + this.f79b.x, (cos1 * radius) + this.f79b.y, (sin2 * radius) + this.f79b.x, (cos2 * radius) + this.f79b.y, (sin3 * radius) + this.f79b.x, (cos3 * radius) + this.f79b.y, (sin1 * radius) + this.f79b.x, (cos1 * radius) + this.f79b.y}, PAINT);
   }
}
