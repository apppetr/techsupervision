package ru.lihachev.norm31937.utils.vectors.impl;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import ru.lihachev.norm31937.utils.vectors.Painter;
import ru.lihachev.norm31937.utils.vectors.Painting;

public class OvalInstrument implements Painter {
   RectF buff = new RectF();
   Paint paint = new Paint(1);
   Point start = new Point();

   public OvalInstrument() {
      this.paint.setColor(-1439432909);
   }

   public void handleEvent(Painting target, MotionEvent event) {
      switch (event.getAction()) {
         case 0:
            this.start.set((int) event.getX(), (int) event.getY());
            return;
         case 1:
            target.addPatch(new OvalImagePatch(new RectF(Math.min((float) this.start.x, event.getX()), Math.min((float) this.start.y, event.getY()), Math.max((float) this.start.x, event.getX()), Math.max((float) this.start.y, event.getY()))));
            return;
         case 2:
            this.buff.set((float) this.start.x, (float) this.start.y, event.getX(), event.getY());
            target.getBuffer().drawOval(this.buff, this.paint);
            target.postInvalidate();
            return;
         default:
            return;
      }
   }
}

