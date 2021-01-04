package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class PointItemPatch implements ImagePatch {
   private static final Paint PAINT = new Paint(1);
   Point position;

   static {
      PAINT.setColor(-1426115789);
   }

   public PointItemPatch(Point position2) {
      this.position = position2;
   }

   public void draw(Painting target, Canvas cvs) {
      cvs.drawCircle((float) this.position.x, (float) this.position.y, target.getDensity() * 5.0f, PAINT);
   }
}