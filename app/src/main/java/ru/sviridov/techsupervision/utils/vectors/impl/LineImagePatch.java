package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class LineImagePatch implements ImagePatch {
   private static final Paint PAINT = new Paint(1);

   /* renamed from: a */
   private final Point f84a;

   /* renamed from: b */
   private final Point f85b;

   static {
      PAINT.setColor(-1430519809);
   }

   public LineImagePatch(Point a, Point b) {
      this.f84a = a;
      this.f85b = b;
   }

   public void draw(Painting target, Canvas cvs) {
      PAINT.setStrokeWidth(target.getDensity() * 3.0f);
      cvs.drawLine((float) this.f84a.x, (float) this.f84a.y, (float) this.f85b.x, (float) this.f85b.y, PAINT);
   }
}

