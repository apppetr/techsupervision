package ru.lihachev.norm31937.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.internal.view.SupportMenu;

import ru.lihachev.norm31937.utils.vectors.ImagePatch;
import ru.lihachev.norm31937.utils.vectors.Painting;
public class LineFImagePatch implements ImagePatch {
   private static final Paint PAINT = new Paint(1);

   /* renamed from: a */
   private final PointF f82a;

   /* renamed from: b */
   private final PointF f83b;

   static {
      PAINT.setColor(SupportMenu.CATEGORY_MASK);
   }

   public LineFImagePatch(PointF a, PointF b) {
      this.f82a = a;
      this.f83b = b;
   }

   public void set(PointF a, PointF b) {
      this.f82a.set(a);
      this.f83b.set(b);
   }

   public void draw(Painting target, Canvas cvs) {
      PAINT.setStrokeWidth(target.getDensity() * 2.0f);
      cvs.drawLine(this.f82a.x, this.f82a.y, this.f83b.x, this.f83b.y, PAINT);
   }
}
