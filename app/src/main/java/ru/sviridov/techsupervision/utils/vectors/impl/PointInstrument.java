package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import ru.sviridov.techsupervision.utils.vectors.Painter;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class PointInstrument implements Painter {
   public void handleEvent(Painting target, MotionEvent event) {
      switch (event.getAction()) {
         case 0:
            target.addPatch(new PointItemPatch(new Point((int) event.getX(), (int) event.getY())));
            return;
         default:
            return;
      }
   }
}
