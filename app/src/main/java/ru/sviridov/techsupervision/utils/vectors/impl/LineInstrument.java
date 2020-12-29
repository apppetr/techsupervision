package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import ru.sviridov.techsupervision.utils.vectors.Painter;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class LineInstrument implements Painter {
   Paint paint = new Paint(1);
   Point start = new Point();

   public LineInstrument() {
      this.paint.setColor(-1439484929);
      this.paint.setStrokeWidth(0.0F);
   }

   public void handleEvent(Painting var1, MotionEvent var2) {
      if (this.paint.getStrokeWidth() == 0.0F) {
         this.paint.setStrokeWidth(var1.getDensity() * 2.0F);
      }

      switch(var2.getAction()) {
      case 0:
         this.start.set((int)var2.getX(), (int)var2.getY());
         break;
      case 1:
         var1.addPatch(new ArrowImagePatch(new Point(this.start), new Point((int)var2.getX(), (int)var2.getY())));
         break;
      case 2:
         var1.getBuffer().drawLine((float)this.start.x, (float)this.start.y, var2.getX(), var2.getY(), this.paint);
         var1.postInvalidate();
      }

   }
}
