package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import ru.sviridov.techsupervision.utils.vectors.Painter;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class OvalInstrument implements Painter {
   RectF buff = new RectF();
   Paint paint = new Paint(1);
   Point start = new Point();

   public OvalInstrument() {
      this.paint.setColor(-1439432909);
   }

   public void handleEvent(Painting var1, MotionEvent var2) {
      switch(var2.getAction()) {
      case 0:
         this.start.set((int)var2.getX(), (int)var2.getY());
         break;
      case 1:
         var1.addPatch(new OvalImagePatch(new RectF(Math.min((float)this.start.x, var2.getX()), Math.min((float)this.start.y, var2.getY()), Math.max((float)this.start.x, var2.getX()), Math.max((float)this.start.y, var2.getY()))));
         break;
      case 2:
         this.buff.set((float)this.start.x, (float)this.start.y, var2.getX(), var2.getY());
         var1.getBuffer().drawOval(this.buff, this.paint);
         var1.postInvalidate();
      }

   }
}
