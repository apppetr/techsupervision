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
      PAINT.setStyle(Style.STROKE);
   }

   public OvalImagePatch(RectF var1) {
      this.rect = var1;
   }

   public void draw(Painting var1, Canvas var2) {
      PAINT.setStrokeWidth(var1.getDensity());
      var2.drawOval(this.rect, PAINT);
   }
}
