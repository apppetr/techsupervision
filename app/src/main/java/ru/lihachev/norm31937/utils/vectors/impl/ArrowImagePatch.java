package ru.lihachev.norm31937.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import ru.lihachev.norm31937.utils.vectors.ImagePatch;
import ru.lihachev.norm31937.utils.vectors.Painting;

public class ArrowImagePatch implements ImagePatch {
   private static final float COS120 = ((float) Math.cos(Math.toRadians(120.0d)));
   private static final Paint PAINT = new Paint(1);
   private static final float SIN120 = ((float) Math.sin(Math.toRadians(120.0d)));

   /* renamed from: a */
   private final Point f80a;

   /* renamed from: b */
   private final Point f81b;

   static {
      PAINT.setColor(-1430519809);
      PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
   }

   public ArrowImagePatch(Point a, Point b) {
      this.f80a = a;
      this.f81b = b;
   }

   public void draw(Painting target, Canvas cvs) {
      PAINT.setStrokeWidth(target.getDensity() * 3.0f);
      cvs.drawLine((float) this.f80a.x, (float) this.f80a.y, (float) this.f81b.x, (float) this.f81b.y, PAINT);
      float hypo = (float) Math.sqrt(Math.pow((double) (this.f80a.x - this.f81b.x), 2.0d) + Math.pow((double) (this.f80a.y - this.f81b.y), 2.0d));
      float sin1 = ((float) (-(this.f80a.x - this.f81b.x))) / hypo;
      float cos1 = ((float) (-(this.f80a.y - this.f81b.y))) / hypo;
      float sin2 = (COS120 * sin1) + (SIN120 * cos1);
      float cos2 = (COS120 * cos1) - (SIN120 * sin1);
      float sin3 = (COS120 * sin1) - (SIN120 * cos1);
      float cos3 = (COS120 * cos1) + (SIN120 * sin1);
      float radius = target.getDensity() * 10.0f;
      Canvas canvas = cvs;
      canvas.drawLines(new float[]{(sin1 * radius) + ((float) this.f81b.x), (cos1 * radius) + ((float) this.f81b.y), (sin2 * radius) + ((float) this.f81b.x), (cos2 * radius) + ((float) this.f81b.y), (sin3 * radius) + ((float) this.f81b.x), (cos3 * radius) + ((float) this.f81b.y), (sin1 * radius) + ((float) this.f81b.x), (cos1 * radius) + ((float) this.f81b.y)}, PAINT);
   }
}
