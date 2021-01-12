package ru.lihachev.norm31937.utils.vectors.impl;

import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import ru.lihachev.norm31937.utils.vectors.Painter;
import ru.lihachev.norm31937.utils.vectors.Painting;

public class LineInstrument implements Painter {
   Paint paint = new Paint(1);
   Point start = new Point();

   public LineInstrument() {
      this.paint.setColor(-1439484929);
      this.paint.setStrokeWidth(0.0f);
   }

   public void handleEvent(Painting target, MotionEvent event) {
      if (this.paint.getStrokeWidth() == 0.0f) {
         this.paint.setStrokeWidth(target.getDensity() * 2.0f);
      }
      switch (event.getAction()) {
         case 0:
            this.start.set((int) event.getX(), (int) event.getY());
            return;
         case 1:
            target.addPatch(new ArrowImagePatch(new Point(this.start), new Point((int) event.getX(), (int) event.getY())));
            return;
         case 2:
            target.getBuffer().drawLine((float) this.start.x, (float) this.start.y, event.getX(), event.getY(), this.paint);
            target.postInvalidate();
            return;
         default:
            return;
      }
   }
}
