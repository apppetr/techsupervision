package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class OvalImagePatch implements ImagePatch {
   private static final Paint PAINT = new Paint(1);
   private final RectF rect;

   static {
      PAINT.setColor(-1426080769);
      PAINT.setStyle(Paint.Style.STROKE);
   }

   public OvalImagePatch(RectF rect2) {
      this.rect = rect2;
   }

   public void draw(Painting target, Canvas cvs) {
      PAINT.setStrokeWidth(target.getDensity());
      cvs.drawOval(this.rect, PAINT);
   }
}

